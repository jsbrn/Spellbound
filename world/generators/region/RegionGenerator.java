package world.generators.region;

import world.generators.chunk.ChunkGenerator;

public interface RegionGenerator {

    ChunkGenerator getChunkGenerator(int cx, int cy, int region_size);

}
