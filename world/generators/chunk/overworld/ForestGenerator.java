package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.Tiles;

import java.util.Random;

public class ForestGenerator extends OpenFieldGenerator {
    
    private float density;

    public ForestGenerator(int seed) {
        super(seed);
        this.density = rng().nextFloat();
    }

    @Override
    public byte getTop(int x, int y) {
        if (rng().nextFloat() <= density) return Tiles.TREE;
        return super.getTop(x, y);
    }

    @Override
    public Color getColor() {
        return Color.green.darker(density * 0.25f);
    }
}
