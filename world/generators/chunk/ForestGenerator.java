package world.generators.chunk;

import world.Portal;

import java.util.Random;

public class ForestGenerator extends OpenFieldChunkGenerator {

    private Random rng;

    public ForestGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getTop(int x, int y) {
        if (Math.random() > 0.5f) return 8;
        return (byte)(Math.random() > 0.6 ? (Math.random() < 0.2 ? 4 : 3) : 0);
    }

}
