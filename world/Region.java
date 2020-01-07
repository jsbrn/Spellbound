package world;

import misc.Location;
import misc.Window;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.entities.actions.action.SetAnimationAction;
import world.entities.magic.MagicSource;
import world.events.EventDispatcher;
import world.events.EventListener;
import world.events.event.EntityMoveEvent;
import world.generators.chunk.ChunkType;
import world.generators.region.RegionGenerator;

import java.util.ArrayList;

public class Region {

    private String name;
    private Chunk[][] chunks;
    private ChunkType[][] chunk_map;
    private int size;

    private ArrayList<MagicSource> magic_sources;

    public Region(String name, int size, RegionGenerator generator) {

        this.name = name;
        this.size = size;

        chunks = new Chunk[size][size];
        chunk_map = new ChunkType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                chunk_map[i][j] = generator.getChunkType(i, j, size);
            }
        }
        magic_sources = new ArrayList<>();

        Region that = this;
        EventDispatcher.register(new EventListener().on(EntityMoveEvent.class.toString(), e -> {

            EntityMoveEvent event = (EntityMoveEvent) e;
            Entity entity = event.getEntity();
            Location location = entity.getLocation();

            if (!entity.getLocation().getRegion().equals(that)) return;

            double[] coords = location.getCoordinates();
            int[] chcoords = location.getChunk().getCoordinates();

            int cdx = 0, cdy = 0;
            if (coords[0] == Chunk.CHUNK_SIZE - 1 && chcoords[0] < size - 1) cdx = 1;
            if (coords[0] == 0 && chcoords[0] > 0) cdx = -1;
            if (coords[1] == Chunk.CHUNK_SIZE - 1 && chcoords[1] < size - 1) cdy = 1;
            if (coords[1] == 0 && chcoords[1] > 0) cdy = -1;


            if (cdx != 0 || cdy != 0) {
                System.out.println("Moving to "+this.getName()+" @ ["+(chcoords[0]+cdx)+", "+(chcoords[1]+cdy)+"]");
                entity.moveTo(new Location(
                        this,
                        getChunk(chcoords[0] + cdx, chcoords[1] + cdy),
                        (coords[0] + Chunk.CHUNK_SIZE + cdx) % Chunk.CHUNK_SIZE,
                        (coords[1] + Chunk.CHUNK_SIZE + cdy) % Chunk.CHUNK_SIZE));
                entity.queueAction(new SetAnimationAction("walking", false));
                entity.move(cdx, cdy);
                entity.queueAction(new SetAnimationAction("idle", false));
            }

        }));

    }

    /**
     * Find the portal that links to the portal specified. Forcefully generates chunks until it finds the portal.
     * @param destination Region of destination portal.
     * @param portal_name Name of destination portal.
     * @return Portal if found, null if not
     */
    public Portal findPortalTo(Region destination, String portal_name) {
        for (int i = 0; i < chunk_map.length; i++) {
            for (int j = 0; j < chunk_map[i].length; j++) {
                Chunk c = getChunk(i, j);
                if (c != null) {
                    Portal p = c.findPortalTo(destination, portal_name);
                    if (p != null) return p;
                }
            }
        }
        return null;
    }

    public byte[] getTile(int tx, int ty) {
        Chunk current = World.getPlayer().getLocation().getChunk();
        if (current == null) return new byte[2];
        return current.get(tx, ty);
    }

    public Chunk getChunk(int x, int y) {
        if (x < 0 || x >= chunk_map.length || y < 0 || y >= chunk_map[0].length) return null;
        if (chunks[x][y] == null) chunks[x][y] = new Chunk(x, y, chunk_map[x][y]);
        return chunks[x][y];
    }

    public Chunk[][] getAdjacentChunks(int x, int y) {
        return new Chunk[][]{
            new Chunk[]{ getChunk( x-1, y-1), getChunk(x-1, y), getChunk(x-1, y+1) },
            new Chunk[]{ getChunk( x, y-1), getChunk(x, y), getChunk(x, y+1) },
            new Chunk[]{ getChunk( x+1, y-1), getChunk(x+1, y), getChunk(x+1, y+1) }
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
        Chunk[][] adjacent = getAdjacentChunks(
                World.getPlayer().getLocation().getChunk().getCoordinates()[0],
                World.getPlayer().getLocation().getChunk().getCoordinates()[1]);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Chunk adj = adjacent[i + 1][j + 1];
                if (adj == null) continue;
                adj.update();
            }
        }
    }

    public void draw(float ox, float oy, float scale, Graphics g) {
        Chunk[][] adjacent = getAdjacentChunks(
                World.getPlayer().getLocation().getChunk().getCoordinates()[0],
                World.getPlayer().getLocation().getChunk().getCoordinates()[1]);
        float chunk_size = Chunk.CHUNK_SIZE * Chunk.TILE_SIZE * Window.getScale();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Chunk adj = adjacent[i+1][j+1];
                if (adj == null) continue;
                float osx = ox + (i * chunk_size), osy = oy + (j * chunk_size);
                adj.draw(
                        osx,
                        osy,
                        Window.getScale(),
                        i == 0 && j == 0);
            }
        }
        for (int i = 0; i < magic_sources.size(); i++) magic_sources.get(i).draw(ox, oy, scale, g);
    }

    @Override
    public String toString() {
        return name;
    }

}
