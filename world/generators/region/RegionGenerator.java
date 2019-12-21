package world.generators.region;

import world.generators.chunk.ChunkType;

public interface RegionGenerator {

    ChunkType[][] generateChunkMap(int size);

}
