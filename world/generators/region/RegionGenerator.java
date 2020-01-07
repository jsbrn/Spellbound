package world.generators.region;

import world.generators.chunk.ChunkType;

public interface RegionGenerator {

    ChunkType getChunkType(int cx, int cy, int size);

}
