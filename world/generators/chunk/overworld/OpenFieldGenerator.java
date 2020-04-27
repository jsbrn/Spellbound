package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.Portal;
import world.Tiles;
import world.entities.Entity;
import world.generators.chunk.ChunkGenerator;

public class OpenFieldGenerator extends ChunkGenerator {

    public OpenFieldGenerator(int seed) {
        super(seed);
    }

    @Override
    public byte getBase(int x, int y) {
        return 1;
    }

    @Override
    public byte getTop(int x, int y) {
        return (byte)(rng().nextFloat() > 0.6 ? (rng().nextFloat() < 0.2 ? Tiles.FLOWERS : Tiles.TALL_GRASS) : 0);
    }

    @Override
    public Portal getPortal() {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        return null;
    }

    @Override
    public Color getColor() {
        return Color.green;
    }
}
