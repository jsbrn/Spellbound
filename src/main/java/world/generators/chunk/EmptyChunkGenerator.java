package world.generators.chunk;

import com.github.mathiewz.slick.Color;
import world.Portal;
import world.Tiles;

public class EmptyChunkGenerator extends ChunkGenerator {

    public EmptyChunkGenerator(int seed) {
        super(seed);
    }

    @Override
    public byte getBase(int x, int y) {
        return Tiles.AIR;
    }

    @Override
    public byte getTop(int x, int y) {
        return Tiles.AIR;
    }

    @Override
    public Portal getPortal() {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) { return null; }

    @Override
    public Color getColor() {
        return Color.black;
    }

    @Override
    public String getIcon() {
        return null;
    }
}
