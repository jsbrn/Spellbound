package world.generators.chunk;

import world.Portal;

import java.util.Random;

public class OpenFieldChunkGenerator extends ChunkGenerator {

    private Random rng;

    public OpenFieldChunkGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        return 1;
    }

    @Override
    public byte getTop(int x, int y) {
        return (byte)(rng.nextFloat() > 0.6 ? (rng.nextFloat() < 0.2 ? 4 : 3) : 0);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }
}
