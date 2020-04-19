package world.generators.region;

import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.interiors.PlayerHomeGenerator;

public class PlayerHomeRegionGenerator extends RegionGenerator {

    public PlayerHomeRegionGenerator(int seed) {
        super(seed);
    }

    @Override
    public ChunkGenerator getChunkGenerator(int cx, int cy, int size) {
        return new PlayerHomeGenerator(getSeed());
    }

}
