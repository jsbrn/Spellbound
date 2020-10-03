package world;

import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Sound;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import misc.annotations.ServerExecution;
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
    private Sound backgroundAmbience;
    private RegionGenerator generator;

    public Region(String name, RegionGenerator generator) {
        this.generator = generator;
        this.name = name;
        this.backgroundAmbience = generator.getBackgroundAmbience();
        this.portals = new ArrayList<>();
        this.chunks = new ArrayList<>();
    }

    public void setWorld(World w) {
        this.world = w;
    }
    public World getWorld() { return world; }

    public Sound getBackgroundAmbience() {
        return backgroundAmbience;
    }

    public ArrayList<Integer> getEntitiesNear(int entityID, int chunkRadius) {
        Location eLoc = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entityID)).getLocation();
        ArrayList<Chunk> adj = getChunks(eLoc.getChunkCoordinates()[0], eLoc.getChunkCoordinates()[1], chunkRadius);
        ArrayList<Integer> entitiesNear = new ArrayList<>();
        for (Chunk a: adj) entitiesNear.addAll(a.getCachedEntities());
        return entitiesNear;
    }

    public List<Integer> getEntities(double wx, double wy, double width, double height) {
        ArrayList<Integer> chunkEntities = new ArrayList<>();
        int cx = (int)Math.floor(wx / Chunk.CHUNK_SIZE),
            cy = (int)Math.floor(wy / Chunk.CHUNK_SIZE),
            cw = (int)Math.floor(width / Chunk.CHUNK_SIZE),
            ch = (int)Math.floor(height / Chunk.CHUNK_SIZE);
        for (int i = cy; i <= cy + cw; i++) {
            for (int j = cy; j <= cy + ch; j++) {
                chunkEntities.addAll(getChunk(cx, cy).getCachedEntities());
            }
        }
        return chunkEntities.stream().filter(eid -> {
            Location location = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, eid)).getLocation();
            HitboxComponent hitbox = (HitboxComponent)world.getEntities().getComponent(HitboxComponent.class, eid);
            return MiscMath.rectanglesIntersect(
                    location.getCoordinates()[0] - (hitbox.getRadius()/2),
                    location.getCoordinates()[1] - (hitbox.getRadius()/2),
                    (int)hitbox.getRadius(),
                    (int)hitbox.getRadius(),
                    wx,
                    wy,
                    (int)width,
                    (int)height);
        }).collect(Collectors.toList());
    }

    public List<Integer> getEntities(double wx, double wy, double radius) {
        List<Integer> foundEntities = getEntities((int)Math.floor(wx - radius), (int)Math.floor(wy - radius), (int)Math.ceil(radius * 2), (int)Math.ceil(radius * 2));
        return foundEntities.stream().filter(e -> {
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

    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    /**
     * Find the chunk with the offset value specified.
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

    public Chunk getChunk(Location location) {
        if (!name.equals(location.getRegionName())) return null;
        return getChunk(location.getChunkCoordinates()[0], location.getChunkCoordinates()[1]);
    }

    /**
     * Gets the chunk at chunk coords (x, y). Uses a binary search algorithm.
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
        //if the chunk is beyond the first and last, return null
        //if the chunk is the first or last, return the first or last, respectively
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
     * Adds (and sorts) a chunk to the list of sectors.
     *
     * @param s The chunk to add.
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
     * Given a chunk (x, y), determine the index it needs to enter the list at,
     * to keep the list sorted. If the chunk is found to already exist from the list,
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

    public byte[] getTile(Location location) {
        return getTile((int)Math.floor(location.getCoordinates()[0]), (int)Math.floor(location.getCoordinates()[1]));
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

    public ArrayList<Chunk> getChunks(int cx, int cy, int cr) {
        ArrayList<Chunk> adj = new ArrayList<>();
        for (int y = cy - cr; y <= cy + cr; y++) {
            for (int x = cx - cr; x <= cx + cr; x++) {
                adj.add(getChunk(cx, cy));
            }
        }
        return adj;
    }

    public String getName() { return name; }

    @ServerExecution
    public void update() {
        int radius = 1;
        Set<Integer> playerEntities = world.getEntities().getEntitiesWith(PlayerComponent.class, LocationComponent.class);
        for (Integer eID: playerEntities) {
            for (int j = -radius; j <= radius; j++) {
                for (int i = -radius; i <= radius; i++) {
                    Location loc = ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, eID)).getLocation();
                    int cx = loc.getChunkCoordinates()[0] + i;
                    int cy = loc.getChunkCoordinates()[1] + j;
                    Chunk adj = getChunk(cx, cy);
                    if (adj.isEmpty())
                        adj.generate();
                }
            }
        }
    }

    public void draw(float scale, Graphics g) {
        int[] pchcoords = Camera.getLocation().getChunkCoordinates();
        float[] oscoords = Camera.getOnscreenCoordinates(0, 0, scale);

        float chunk_size = Chunk.CHUNK_SIZE * Chunk.TILE_SIZE * Window.getScale();
        int radius = 2;

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
                    }
                }
            }
        }
    }

    public void drawDebug(float scale, Graphics g) {
        int[] pchcoords = Camera.getLocation().getChunkCoordinates();
        float[] oscoords = Camera.getOnscreenCoordinates(0, 0, scale);

        float chunk_size = Chunk.CHUNK_SIZE * Chunk.TILE_SIZE * Window.getScale();
        int radius = 1;
        for (int cj = -radius; cj <= radius; cj++) {
            for (int ci = -radius; ci <= radius; ci++) {
                int cx = pchcoords[0] + ci;
                int cy = pchcoords[1] + cj;
                Chunk adj = getChunk(cx, cy);
                if (adj == null) continue;
                float osx = oscoords[0] + (cx * chunk_size), osy = oscoords[1] + (cy * chunk_size);
                adj.drawDebug(osx, osy, scale, g);
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

}
