package world.generators.region;

import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.overworld.BackyardGenerator;
import world.generators.chunk.overworld.ForestGenerator;
import world.generators.chunk.overworld.OpenFieldGenerator;
import world.generators.chunk.overworld.TrapdoorFieldGenerator;

public class DefaultWorldGenerator implements RegionGenerator {

    public ChunkGenerator getChunkGenerator(int x, int y, int size) {
        if (x == size/2 && y == size/2) return new BackyardGenerator();
        if (x == size/2 && y == (size/2) + 1) return new TrapdoorFieldGenerator();
        return Math.random() <= 0.05
                ? new TrapdoorFieldGenerator()
                : (Math.random() < 0.25f ? new OpenFieldGenerator() : new ForestGenerator());
    }

}
