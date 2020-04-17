package world.generators.region;

import misc.MiscMath;
import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.overworld.*;

public class DefaultWorldGenerator implements RegionGenerator {

    public ChunkGenerator getChunkGenerator(int x, int y, int size) {
        if (x == size/2 && y == size/2) return new BackyardGenerator();
        if (x == size/2 && y == (size/2) + 1) return new TrapdoorFieldGenerator(1);
        if (Math.random() <= 0.05) {
            return Math.random() < 0.5 ? new GraveyardGenerator() : new WishingWellFieldGenerator();
        }
        return Math.random() <= 0.05
                ? new TrapdoorFieldGenerator((int)(MiscMath.distance(x, y, size/2, size/2)) / 2)
                : (Math.random() < 0.25f ? new OpenFieldGenerator() : new ForestGenerator());
    }

}
