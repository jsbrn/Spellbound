package world.generators.chunk;

import java.util.Random;

public class ForestGenerator extends ChunkGenerator {

    private Random rng;

    public ForestGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte[][] generateBase(int size) {
        byte[][] base = new byte[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                base[i][j] = 1;
            }
        }
        return base;
    }

    @Override
    public byte[][] generateObjects(int size) {
        byte[][] top = new byte[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Math.random() > 0.5f) { top[i][j] = 8; continue; }
                top[i][j] = (byte)(Math.random() > 0.6 ? (Math.random() < 0.2 ? 4 : 3) : 0);
            }
        }
        return top;
    }

}
