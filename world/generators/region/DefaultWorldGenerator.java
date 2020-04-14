package world.generators.region;

import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.overworld.*;

public class DefaultWorldGenerator implements RegionGenerator {

    public ChunkGenerator getChunkGenerator(int x, int y, int size) {
        if (x == size/2 && y == size/2) return new BackyardGenerator();
        if (x == size/2 && y == (size/2) + 1) return new TrapdoorFieldGenerator();
        if (Math.random() <= 0.05) return new GraveyardGenerator();
        return Math.random() <= 0.05
                ? new TrapdoorFieldGenerator()
                : (Math.random() < 0.25f ? new OpenFieldGenerator() : new ForestGenerator());
    }

}
