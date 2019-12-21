package world;

import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.entities.actions.action.SetAnimationAction;
import world.entities.magic.MagicSource;
import world.entities.types.humanoids.Player;
import world.events.EventDispatcher;
import world.events.EventListener;
import world.events.event.EntityMoveEvent;
import world.generators.chunk.ChunkType;
import world.generators.region.DefaultWorldGenerator;
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
        chunk_map = generator.generateChunkMap(size);
        magic_sources = new ArrayList<>();

        EventDispatcher.register(new EventListener().on(EntityMoveEvent.class.toString(), e -> {
            EntityMoveEvent event = (EntityMoveEvent) e;
            Entity entity = event.getEntity();
            double[] coords = entity.getCoordinates();
            int[] chcoords = entity.getChunkCoordinates();
            int cdx = 0, cdy = 0;
            if (coords[0] == Chunk.CHUNK_SIZE - 1 && chcoords[0] < size - 1) cdx = 1;
            if (coords[0] == 0 && chcoords[0] > 0) cdx = -1;
            if (coords[1] == Chunk.CHUNK_SIZE - 1 && chcoords[1] < size - 1) cdy = 1;
            if (coords[1] == 0 && chcoords[1] > 0) cdy = -1;
            entity.setCoordinates(
                    (coords[0] + Chunk.CHUNK_SIZE + cdx) % Chunk.CHUNK_SIZE,
                    (coords[1] + Chunk.CHUNK_SIZE + cdy) % Chunk.CHUNK_SIZE);
            getChunk(entity.getChunkCoordinates()[0], entity.getChunkCoordinates()[1]).remove(entity);
            getChunk(chcoords[0] + cdx, chcoords[1] + cdy).add(entity);
            if (cdx != 0 || cdy != 0) {
                entity.queueAction(new SetAnimationAction("walking", false));
                entity.move(cdx, cdy);
                entity.queueAction(new SetAnimationAction("idle", false));
            }
        }));
    }

    public void addEntity(Entity e, int cx, int cy, int tx, int ty) {
        getChunk(cx, cy).add(e);
        e.setRegion(name);
        e.setCoordinates(tx, ty);
    }

    public void removeEntity(Entity e) {
        int[] chcoords = e.getChunkCoordinates();
        getChunk(chcoords[0], chcoords[1]).remove(e);
    }

    public byte[] getTile(int tx, int ty) {
        Chunk current = getChunk(World.getPlayer().getChunkCoordinates()[0], World.getPlayer().getChunkCoordinates()[1]);
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
        Chunk[][] adjacent = getAdjacentChunks(World.getPlayer().getChunkCoordinates()[0], World.getPlayer().getChunkCoordinates()[1]);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Chunk adj = adjacent[i + 1][j + 1];
                if (adj == null) continue;
                adj.update();
            }
        }
    }

    public void draw(float ox, float oy, float scale, Graphics g) {
        Chunk[][] adjacent = getAdjacentChunks(World.getPlayer().getChunkCoordinates()[0], World.getPlayer().getChunkCoordinates()[1]);
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

}
