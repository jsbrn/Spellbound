package world.generators.chunk;

import org.newdawn.slick.Color;
import world.Portal;
import world.entities.Entity;

import java.util.Random;

public abstract class ChunkGenerator {

    private int cx, cy;
    private Random rng;

    public ChunkGenerator(int seed) {
        this.rng = new Random(seed);
    }

    public Random rng() { return rng; }

    public final void setCX(int cx) { this.cx = cx; }
    public final void setCY(int cy) { this.cy = cy; }
    public int getCX() { return cx; }
    public int getCY() { return cy; }

    public abstract byte getBase(int x, int y);
    public abstract byte getTop(int x, int y);
    public abstract Portal getPortal(int x, int y);
    public abstract Entity getEntity(int x, int y);

    public abstract Color getColor();

}
