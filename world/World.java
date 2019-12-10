package world;

import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.generators.chunk.ChunkType;
import world.generators.world.DefaultWorldGenerator;
import world.generators.world.WorldGenerator;

public class World {

    private static Chunk[][] chunks;
    private static ChunkType[][] chunk_map;

    private static Entity player;

    public static void init(int size) {
        generate(size, new DefaultWorldGenerator());
        player = new Entity();

    }

    public static Chunk getChunk(int x, int y) {
        if (chunks[x][y] == null) chunks[x][y] = new Chunk(chunk_map[x][y]);
        return chunks[x][y];
    }

    public static Entity getPlayer() {
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
