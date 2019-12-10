package world;

import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.entities.types.Player;
import world.events.EventDispatcher;
import world.events.EventListener;
import world.events.event.EntityMoveEvent;
import world.generators.chunk.ChunkType;
import world.generators.world.DefaultWorldGenerator;
import world.generators.world.WorldGenerator;

public class World {

    private static Chunk[][] chunks;
    private static ChunkType[][] chunk_map;

    private static Player player;

    public static void init(int size) {
        generate(size, new DefaultWorldGenerator());
        player = new Player();
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
            System.out.println(coords[0]+", "+cdx+", "+((coords[0] + cdx) % Chunk.CHUNK_SIZE));
            entity.setCoordinates(
                    (coords[0] + Chunk.CHUNK_SIZE + cdx) % Chunk.CHUNK_SIZE,
                    (coords[1] + Chunk.CHUNK_SIZE + cdy) % Chunk.CHUNK_SIZE);
            entity.setChunkCoordinates(chcoords[0] + cdx, chcoords[1] + cdy);
            if (cdx != 0 || cdy != 0) entity.move(cdx, cdy);
        }));
    }

    public static Chunk getChunk(int x, int y) {
        if (chunks[x][y] == null) chunks[x][y] = new Chunk(chunk_map[x][y]);
        return chunks[x][y];
    }

    public static Player getPlayer() {
        return player;
    }

    public static void update() {
        player.update();
    }

    public static void generate(int size, WorldGenerator generator) {
        chunks = new Chunk[size][size];
        chunk_map = generator.generateChunkMap(size);
    }

    public static void draw(float ox, float oy, float scale, Graphics g) {
        Chunk current = getChunk(player.getChunkCoordinates()[0], player.getChunkCoordinates()[1]);
        current.draw(ox, oy, scale);
        player.draw(ox, oy, scale);
        g.drawString("PLAYER DEBUG = "+ player.debug(), 0, 0);
        g.drawString("CC = " + player.getChunkCoordinates()[0] + ", " + player.getChunkCoordinates()[1], 0, 20);
        g.drawString("BASE = " + current.get((int)player.getCoordinates()[0], (int)player.getCoordinates()[1])[0], 0, 40);
        g.drawString("OBJ = " + current.get((int)player.getCoordinates()[0], (int)player.getCoordinates()[1])[1], 0, 60);
    }

}
