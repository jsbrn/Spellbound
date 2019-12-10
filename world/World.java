package world;

import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.generators.chunk.ChunkType;
import world.generators.world.DefaultWorldGenerator;
import world.generators.world.WorldGenerator;

public class World {

    private int size;
    private Chunk[][] chunks;
    private ChunkType[][] chunk_map;

    private Entity player;

    public World(int size) {
        this.size = size;
        this.chunks = new Chunk[size][size];
        this.player = new Entity();
        generate(new DefaultWorldGenerator());
    }

    public Chunk get(int x, int y) {
        if (chunks[x][y] == null) chunks[x][y] = new Chunk(chunk_map[x][y]);
        return chunks[x][y];
    }

    public Entity getPlayer() {
        return player;
    }

    public void update() {
        player.move();
    }

    public void generate(WorldGenerator generator) {
        chunk_map = generator.generateChunkMap(size);
    }

    public void draw(float ox, float oy, float scale, Graphics g) {
        Chunk current = get(0, 0);
        current.draw(ox, oy, scale);
        player.draw(ox, oy, scale);
        g.drawString(player.debug(), 0, 0);
    }

}
