package world;

import assets.Assets;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.entities.magic.MagicSource;
import world.events.EventDispatcher;
import world.events.EventListener;
import world.events.event.EntityMovedEvent;
import world.generators.chunk.ChunkGenerator;
import world.generators.region.RegionGenerator;

import java.util.ArrayList;

public class Region {

    private String name;
    private Chunk[][] chunks;
    private ChunkGenerator[][] chunkGenerators;
    private int size;

    private ArrayList<MagicSource> magic_sources;
    private ArrayList<Entity> entities;

    public Region(String name, int size, RegionGenerator generator) {

        this.name = name;
        this.size = size;

        chunks = new Chunk[size][size];
        chunkGenerators = new ChunkGenerator[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunkGenerators[i][j] = generator.getChunkGenerator(i, j, size);
            }
        }

        magic_sources = new ArrayList<>();
        entities = new ArrayList<>();

        Region that = this;
        EventDispatcher.register(new EventListener().on(EntityMovedEvent.class.toString(), e -> {

            EntityMovedEvent event = (EntityMovedEvent) e;

            if (entities.contains(event.getEntity())) {
                removeEntity(event.getEntity());
                addEntity(event.getEntity());
            }

        }));

    }

    public void addEntity(Entity e) {
        int index = getEntityIndex(e.getLocation().getGlobalIndex(), 0, entities.size());
        entities.add(index, e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public ArrayList<Entity> getEntities(int wx, int wy, int width, int height) {
        ArrayList<Entity> subsection = new ArrayList<>();
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                int loc = MiscMath.getIndex(wx + i, wy + j, Chunk.CHUNK_SIZE * getSize());
//                int[] indices = getEntityIndices(loc, loc);
//                for (int e = indices[0]; e < indices[1]; e++) {
//                    Entity entity = entities.get(e);
//                    if (!subsection.contains(entity)) subsection.add(entity);
//                }
//            }
//        }
        int minloc = MiscMath.getIndex(wx, wy, Chunk.CHUNK_SIZE * getSize());
        int maxloc = MiscMath.getIndex(wx + width, wy + height, Chunk.CHUNK_SIZE * getSize());
        int[] indices = getEntityIndices(minloc, maxloc);
        for (int i = indices[0]; i < indices[1]; i++) {
            Entity e = entities.get(i);
            if (subsection.contains(e)) continue;
            boolean intersects = MiscMath.pointIntersectsRect(
                    e.getLocation().getCoordinates()[0],
                    e.getLocation().getCoordinates()[1],
                    wx,
                    wy,
                    width,
                    height
            );
            if (intersects) subsection.add(e);
        }
        return subsection;
    }

    public int[] getEntityIndices(int min_location, int max_location) {
        return new int[]{
                getEntityIndex(min_location-1, 0, entities.size()),
                getEntityIndex(max_location + 1, 0, entities.size()),
        };
    }

    public ArrayList<Entity> getEntities() { return entities; }

    private int getEntityIndex(double location, int min, int max) {

        if (entities.isEmpty()) return 0;

        int half = min + ((max-min) / 2);
        double minloc = entities.get(min).getLocation().getGlobalIndex();
        double maxloc = entities.get(max-1).getLocation().getGlobalIndex();
        double halfloc = entities.get(half).getLocation().getGlobalIndex();

        if (location >= maxloc) return max;
        if (location <= minloc) return min;
        if (max - min == 1) return min + 1;

        if (halfloc > location) {
            return getEntityIndex(location, min, half);
        } else {
            return getEntityIndex(location, half, max);
        }

    }

    /**
     * Find the portal that links to the portal specified. Forcefully generates chunks until it finds the portal.
     * @param destination Region of destination portal.
     * @param portal_name Name of destination portal.
     * @return Portal if found, null if not
     */
    public Portal findPortalTo(Region destination, String portal_name) {
        for (int i = 0; i < chunkGenerators.length; i++) {
            for (int j = 0; j < chunkGenerators[i].length; j++) {
                Chunk c = getChunk(i, j);
                if (c != null) {
                    Portal p = c.findPortalTo(destination, portal_name);
                    if (p != null) return p;
                }
            }
        }
        return null;
    }

    public byte[] getTile(int wx, int wy) {
        Chunk current = getChunk(wx / Chunk.CHUNK_SIZE, wy / Chunk.CHUNK_SIZE);
        if (current == null) return new byte[2];
        return current.get(wx % Chunk.CHUNK_SIZE, wy % Chunk.CHUNK_SIZE);
    }

    public Chunk getChunk(int cx, int cy) {
        if (cx < 0 || cx >= chunkGenerators.length || cy < 0 || cy >= chunkGenerators[0].length) return null;
        if (chunks[cx][cy] == null) chunks[cx][cy] = new Chunk(cx, cy, chunkGenerators[cx][cy]);
        return chunks[cx][cy];
    }

    public Chunk[][] getAdjacentChunks(int cx, int cy) {
        return new Chunk[][]{
            new Chunk[]{ getChunk( cx-1, cy-1), getChunk(cx-1, cy), getChunk(cx-1, cy+1) },
            new Chunk[]{ getChunk( cx, cy-1), getChunk(cx, cy), getChunk(cx, cy+1) },
            new Chunk[]{ getChunk( cx+1, cy-1), getChunk(cx+1, cy), getChunk(cx+1, cy+1) }
        };
    }

    public String getName() { return name; }

    public void addMagicSource(MagicSource magicSource) {
        magic_sources.add(magicSource);
    }

    public void update() {

        for (int i = magic_sources.size() - 1; i >= 0; i--) {
            MagicSource magicSource = magic_sources.get(i);
            magicSource.update();
            if (magicSource.getBody().isDepleted()) magic_sources.remove(i);
        }

        int radius = 2;
        int[] pchcoords = World.getPlayer().getLocation().getChunk().getCoordinates();
        for (int j = -radius; j <= radius; j++) {
            for (int i = -radius; i <= radius; i++) {
                int cx = pchcoords[0] + i;
                int cy = pchcoords[1] + j;
                Chunk adj = getChunk(cx, cy);
                if (adj != null) adj.update();
            }
        }

    }

    public void draw(float scale, Graphics g, boolean debug) {
        int[] pchcoords = World.getPlayer().getLocation().getChunk().getCoordinates();
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
                        if (debug && cx == pchcoords[0] && cy == pchcoords[1]) adj.drawDebug(osx, osy, scale, g);
                    }
                }
            }
        }
        Assets.PARTICLE.startUse();
        for (int i = 0; i < magic_sources.size(); i++) magic_sources.get(i).draw(oscoords[0], oscoords[1], scale);
        Assets.PARTICLE.endUse();
    }

    public int getSize() { return size; }

    public void drawDebug(float scale, Graphics g) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.drawDebug(scale, g);
            g.drawString(
                    e.getClass().getSimpleName()+" "+e.getLocation().getGlobalIndex(),
                    Window.getWidth() - 200, i * 20);
        }
        for (MagicSource magicSource: magic_sources) magicSource.getBody().drawDebug(0, 0, scale, g);
    }

    @Override
    public String toString() {
        return name;
    }

}
