package world;

import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Sound;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import network.MPServer;
import world.entities.components.HitboxComponent;
import world.entities.components.LocationComponent;
import world.entities.components.PlayerComponent;
import world.generation.region.RegionGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Region {

    private World world;

    private String name;
    private ArrayList<Chunk> chunks;

    private ArrayList<Portal> portals;
    private ArrayList<Integer> entities;

    private Sound backgroundAmbience;

    private RegionGenerator generator;

    public Region(String name, RegionGenerator generator) {
        this.generator = generator;
        this.name = name;
        this.backgroundAmbience = generator.getBackgroundAmbience();
        this.entities = new ArrayList<>();
        this.portals = new ArrayList<>();
        this.chunks = new ArrayList<>();
    }

    public void setWorld(World w) {
        this.world = w;
    }
    public Sound getBackgroundAmbience() {
        return backgroundAmbience;
    }

    public int addEntity(Integer e) {
        if (entities.contains(e)) return -1;
        double lindex = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, e)).getLocation().getIndex(this);
        int index = getEntityIndex(lindex, 0, entities.size());
        entities.add(index, e);
        return index;
    }

    public boolean removeEntity(Integer e) {
        return entities.remove(e);
    }

    public ArrayList<Integer> getEntitiesNear(int entityID, int chunkRadius) {
        Location ploc = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entityID)).getLocation();
        return getEntities((
                        ploc.getChunkCoordinates()[0] - chunkRadius) * Chunk.CHUNK_SIZE,
                (ploc.getChunkCoordinates()[1] - chunkRadius) * Chunk.CHUNK_SIZE,
                Chunk.CHUNK_SIZE * ((chunkRadius * 2) + 1),
                Chunk.CHUNK_SIZE * ((chunkRadius * 2) + 1));
    }

    public ArrayList<Integer> getEntities(int wx, int wy, int width, int height) {
        ArrayList<Integer> subsection = new ArrayList<>();
        double minloc = MiscMath.getIndex(wx, wy, Integer.MAX_VALUE);
        double maxloc = MiscMath.getIndex(wx + width, wy + height, Integer.MAX_VALUE);
        int[] indices = getEntityIndices(minloc, maxloc);
        for (int i = indices[0]; i < indices[1]; i++) {
            Integer entity = entities.get(i);
            Location location = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entity)).getLocation();
            if (subsection.contains(entity)) continue;
            boolean intersects = MiscMath.pointIntersectsRect(
                    location.getCoordinates()[0],
                    location.getCoordinates()[1],
                    wx,
                    wy,
                    width,
                    height
            );
            if (intersects) subsection.add(entity);
        }
        return subsection;
    }

    public List<Integer> getEntities(double wx, double wy, double radius) {
        ArrayList<Integer> entities = getEntities(
                (int)(wx - radius), (int)(wy - radius),
                (int)((radius * 2) + 1), (int)((radius * 2) + 2));
        return entities.stream().filter(e -> {
            Location location = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, e)).getLocation();
            HitboxComponent hitbox = (HitboxComponent)world.getEntities().getComponent(HitboxComponent.class, e);
            return MiscMath.circlesIntersect(
                location.getCoordinates()[0],
                location.getCoordinates()[1],
                hitbox.getRadius(),
                wx,
                wy,
                radius);
        }).collect(Collectors.toList());
    }

    public int[] getEntityIndices(double min_location, double max_location) {
        return new int[]{
                getEntityIndex(min_location - 1, 0, entities.size()),
                getEntityIndex(max_location + 1, 0, entities.size()),
        };
    }

    public ArrayList<Integer> getEntities() { return entities; }

    private int getEntityIndex(double location, int min, int max) {

        if (entities.isEmpty()) return 0;

        int half = min + ((max-min) / 2);

        double minloc = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entities.get(min))).getLocation().getIndex(this);
        double maxloc = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entities.get(max - 1))).getLocation().getIndex(this);
        double halfloc = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entities.get(half))).getLocation().getIndex(this);

        if (location >= maxloc) return max;
        if (location <= minloc) return min;
        if (max - min == 1) return min + 1;

        if (halfloc > location) {
            return getEntityIndex(location, min, half);
        } else {
            return getEntityIndex(location, half, max);
        }

    }

    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    /**
     * Find the sector with the offset value specified.
     *
     * @param x The offset (from sectors) from the origin.
     * @param y The offset (from sectors) from the origin.
     * @return A Chunk instance, or null if not found.
     */
    public Chunk getChunk(int x, int y) {
        Chunk s = getChunk(x, y, 0, chunks.size() - 1, chunks);
        if (s == null) {
            s = new Chunk(x, y, this);
            addChunk(s);
        }
        return s;
    }

    /**
     * Gets the sector at sector coords (x, y). Uses a binary search algorithm.
     *
     * @param x    Chunk coordinate.
     * @param y    Chunk coordinate.
     * @param l    The lower bound of the list to search (start with 0).
     * @param u    The upper bound of the list to search (start with list.size - 1).
     * @param list The list to search.
     * @return A Chunk instance, or null if none found at (x, y).
     */
    private Chunk getChunk(int x, int y, int l, int u, ArrayList<Chunk> list) {
        if (list.isEmpty()) return null;
        //if the sector is beyond the first and last, return null
        //if the sector is the first or last, return the first or last, respectively
        if (list.get(0).compareTo(x, y) > 0 || list.get(list.size() - 1).compareTo(x, y) < 0) return null;
        if (list.get(u).getCoordinates()[0] == x && list.get(u).getCoordinates()[1] == y) return list.get(u);
        if (list.get(l).getCoordinates()[0] == x && list.get(l).getCoordinates()[1] == y) return list.get(l);

        int lsize = (u + 1) - l;
        int index = lsize / 2 + l;

        if (lsize == 0) return null;

        Chunk element = list.get(index);
        int cmp = element.compareTo(x, y);

        if (cmp == 0) return list.get(index);

        int sub_bounds[] = new int[]{cmp > 0 ? l : index, cmp > 0 ? index : u};
        if ((sub_bounds[1] + 1) - sub_bounds[0] <= 2) { //if sublist is two from length
            if (cmp > 0) if (sub_bounds[0] > -1)
                if (list.get(sub_bounds[0]).getCoordinates()[0] == x && list.get(sub_bounds[0]).getCoordinates()[1] == y)
                    return list.get(sub_bounds[0]);
            if (cmp < 0) if (sub_bounds[1] < list.size())
                if (list.get(sub_bounds[1]).getCoordinates()[0] == x && list.get(sub_bounds[1]).getCoordinates()[1] == y)
                    return list.get(sub_bounds[1]);
            return null;
        } else {
            return getChunk(x, y, sub_bounds[0], sub_bounds[1], list);
        }
    }

    /**
     * Adds (and sorts) a sector to the list of sectors.
     *
     * @param s The sector to add.
     * @return A boolean indicating the success of the operation.
     */
    public boolean addChunk(Chunk s) {
        return addChunk(s, 0, chunks.size() - 1);
    }

    private boolean addChunk(Chunk s, int l, int u) {
        int index = getPotentialChunkIndex(s.getCoordinates()[0], s.getCoordinates()[1], l, u);
        if (index <= -1 || index > chunks.size()) {
            return false;
        }
        chunks.add(index, s);

        return true;
    }

    public RegionGenerator getGenerator() {
        return generator;
    }

    /**
     * Given a sector (x, y), determine the index it needs to enter the list at,
     * to keep the list sorted. If the sector is found to already exist from the list,
     * -1 is returned. Uses a binary search algorithm.
     *
     * @param l Lower bound of the search region (when calling first, use 0)
     * @param u Upper bound of the search region (when calling first, use size()-1)
     * @return An integer of the above specifications.
     */
    private int getPotentialChunkIndex(int x, int y, int l, int u) {
        //if the bounds are the number, then return the bound

        if (chunks.isEmpty()) return 0;
        if (chunks.get(0).compareTo(x, y) > 0) return 0;
        if (chunks.get(chunks.size() - 1).compareTo(x, y) < 0) return chunks.size();

        int lsize = (u + 1) - l;
        int index = lsize / 2 + l;

        if (lsize == 0) return -1;

        Chunk element = chunks.get(index);
        int cmp = element.compareTo(x, y);

        if (cmp == 0) return -1;

        int sub_bounds[] = new int[]{cmp > 0 ? l : index, cmp > 0 ? index : u};
        if ((sub_bounds[1] + 1) - sub_bounds[0] <= 2) { //if sublist is two from length
            if (chunks.get(sub_bounds[0]).compareTo(x, y) < 0
                    && chunks.get(sub_bounds[1]).compareTo(x, y) > 0) return sub_bounds[0] + 1;
            return -1;
        } else {
            return getPotentialChunkIndex(x, y, sub_bounds[0], sub_bounds[1]);
        }
    }

    public void registerPortal(Portal portal) {
        portals.add(portal);
    }

    public Portal getPortal(int index) {
        return portals.get(index);
    }

    public Portal getPortal(int wx, int wy) {
        return portals.stream().filter(portal -> {
            int[] coordinates = portal.getCoordinates();
            return coordinates[0] == wx && coordinates[1] == wy;
        }).findFirst().orElse(null);
    }

    /**
     * Find the portal that links to the portal specified.
     * @param destination Region of destination portal.
     * @param destinationName Name of destination portal.
     * @return Portal if found, null if not
     */
    public Portal findPortal(String portalName, Region destination, String destinationName) {
        for (Portal p: portals)
            if (p.getName().equals(portalName) && p.getDestination().equals(destination) && p.getDestinationName().equals(destinationName))
                return p;
        return null;
    }

    public byte[] getTile(int wx, int wy) {
        Chunk current = getChunk(wx / Chunk.CHUNK_SIZE, wy / Chunk.CHUNK_SIZE);
        if (current == null) return new byte[2];
        return current.get(wx % Chunk.CHUNK_SIZE, wy % Chunk.CHUNK_SIZE);
    }

    public boolean isChunkDiscovered(int cx, int cy) {
        return true;
        //TODO: implement per-player chunk discovery at some point
    }

    public Chunk[][] getAdjacentChunks(int cx, int cy) {
        return new Chunk[][]{
            new Chunk[]{ getChunk( cx-1, cy-1), getChunk(cx-1, cy), getChunk(cx-1, cy+1) },
            new Chunk[]{ getChunk( cx, cy-1), getChunk(cx, cy), getChunk(cx, cy+1) },
            new Chunk[]{ getChunk( cx+1, cy-1), getChunk(cx+1, cy), getChunk(cx+1, cy+1) }
        };
    }

    public String getName() { return name; }

    public void update() {
        int radius = 3;
        Set<Integer> playerEntities = MPServer.getWorld().getEntities().getEntitiesWith(entities, PlayerComponent.class, LocationComponent.class);
        for (Integer eID: playerEntities) {
            for (int j = -radius; j <= radius; j++) {
                for (int i = -radius; i <= radius; i++) {
                    Location loc = ((LocationComponent)MPServer.getWorld().getEntities().getComponent(LocationComponent.class, eID)).getLocation();
                    int cx = loc.getChunkCoordinates()[0] + i;
                    int cy = loc.getChunkCoordinates()[1] + j;
                    Chunk adj = getChunk(cx, cy);
                    if (adj.isEmpty()) adj.generate();
                }
            }
        }
    }

    public void draw(float scale, Graphics g) {
        int[] pchcoords = Camera.getLocation().getChunkCoordinates();
        float[] oscoords = Camera.getOnscreenCoordinates(0, 0, scale);

        float chunk_size = Chunk.CHUNK_SIZE * Chunk.TILE_SIZE * Window.getScale();
        int radius = 1;

        for (int pass = 0; pass < 2; pass++) {
            for (int cj = -radius; cj <= radius; cj++) {
                for (int ci = -radius; ci <= radius; ci++) {
                    int cx = pchcoords[0] + ci;
                    int cy = pchcoords[1] + cj;
                    Chunk adj = getChunk(cx, cy);
                    if (adj == null) continue;
                    float osx = oscoords[0] + (cx * chunk_size), osy = oscoords[1] + (cy * chunk_size);
                    if (pass == 0) {
                        adj.drawBase(osx, osy, scale);
                    } else {
                        adj.drawTop(osx, osy, scale);
                        //adj.drawDebug(osx, osy, scale, g);
                    }
                }
            }
        }
    }

    public void drawDebug(float scale, Graphics g) {

    }

    @Override
    public String toString() {
        return name;
    }

}
