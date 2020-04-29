package world.generators.chunk;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Portal;
import world.entities.Entity;

import java.util.Random;

public abstract class ChunkGenerator {

    private int cx, cy, seed;
    private Random rng;

    public ChunkGenerator(int seed) {
        this.rng = new Random(seed);
    }

    public Random rng() { return rng; }

    private void resetRNG() {
        this.rng = new Random(seed + cx + cy);
    }

    public final void setChunkX(int cx) {
        this.cx = cx;
        resetRNG();
    }
    public final void setChunkY(int cy) {
        this.cy = cy;
        resetRNG();
    }
    public int getChunkX() { return cx; }
    public int getChunkY() { return cy; }

    public byte[][] getTiles(boolean top) {
        byte[][] base = new byte[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
        for (int i = 0; i < base.length; i++)
            for (int j = 0; j < base[0].length; j++)
                base[i][j] = top ? getTop(i, j) : getBase(i, j);
        return base;
    }

    public abstract byte getBase(int x, int y);
    public abstract byte getTop(int x, int y);
    public abstract Portal getPortal();
    public abstract Entity getEntity(int x, int y);

    public abstract Color getColor();
    public abstract String getIcon();

}
