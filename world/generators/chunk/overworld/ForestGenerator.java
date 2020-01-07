package world.generators.chunk.overworld;

import assets.definitions.Tile;

import java.util.Random;

public class ForestGenerator extends OpenFieldGenerator {

    private Random rng;

    public ForestGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getTop(int x, int y) {
        if (Math.random() > 0.5f) return Tile.TREE;
        return super.getTop(x, y);
    }

}
