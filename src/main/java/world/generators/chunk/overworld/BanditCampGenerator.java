package world.generators.chunk.overworld;

import com.github.mathiewz.slick.Color;
import misc.MiscMath;
import world.Chunk;
import world.Tiles;

public class BanditCampGenerator extends ForestGenerator {

    public BanditCampGenerator(int seed) {
        super(seed);
    }

    @Override
    public byte getTop(int x, int y) {
        if (y == Chunk.CHUNK_SIZE / 2 && x == Chunk.CHUNK_SIZE / 2) return Tiles.FIRE_PIT;
        if (MiscMath.distance(x, y, Chunk.CHUNK_SIZE/2, Chunk.CHUNK_SIZE/2) < 3 + rng().nextInt(2))
            return rng().nextInt(10) == 0 ? (byte)Tiles.CHAIR : (byte)Tiles.AIR;
        return super.getTop(x, y);
    }

    @Override
    public Color getColor() {
        return Color.green;
    }
}
