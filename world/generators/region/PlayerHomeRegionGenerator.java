package world.generators.region;

import world.generators.chunk.ChunkType;

public class PlayerHomeRegionGenerator implements RegionGenerator {

    @Override
    public ChunkType getChunkType(int cx, int cy, int size) {
        return ChunkType.HOME;
    }

}
