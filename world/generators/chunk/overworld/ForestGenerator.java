package world.generators.chunk.overworld;

import assets.definitions.TileType;

import java.util.Random;

public class ForestGenerator extends OpenFieldGenerator {

    private Random rng;
    private float density;

    public ForestGenerator() {
        this.rng = new Random();
        this.density = rng.nextFloat();
    }

    @Override
    public byte getTop(int x, int y) {
        if (rng.nextFloat() > 1 - density) return TileType.TREE;
        return super.getTop(x, y);
    }

}
