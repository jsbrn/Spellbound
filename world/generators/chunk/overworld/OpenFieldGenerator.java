package world.generators.chunk.overworld;

import assets.definitions.Tile;
import world.Portal;
import world.generators.chunk.ChunkGenerator;

import java.util.Random;

public class OpenFieldGenerator extends ChunkGenerator {

    private Random rng;

    public OpenFieldGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        return 1;
    }

    @Override
    public byte getTop(int x, int y) {
        return (byte)(rng.nextFloat() > 0.6 ? (rng.nextFloat() < 0.2 ? Tile.FLOWERS : Tile.TALL_GRASS) : 0);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }
}
