package world.generators.chunk;

import world.RegionLink;

import java.util.Random;

public class HomeGenerator extends ChunkGenerator {

    private Random rng;

    public HomeGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte[][] generateBase(int size) {
        byte[][] base = new byte[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                base[i][j] = i == 0 ? (byte)11 : (byte)10;
            }
        }
        base[6][5] = 0;
        return base;
    }

    @Override
    public byte[][] generateObjects(int size) {
        byte[][] top = new byte[size][size];
        return top;
    }

    @Override
    public RegionLink[][] generateLinks(int size) {
        //RegionLink[][] toWorld
        return new RegionLink[size][size];
    }

}
