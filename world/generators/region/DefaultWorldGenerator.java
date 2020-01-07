package world.generators.region;

import world.generators.chunk.ChunkType;

public class DefaultWorldGenerator implements RegionGenerator {

    public ChunkType getChunkType(int x, int y, int size) {
        if (x == size/2 && y == size/2) return ChunkType.BACKYARD;
        return Math.random() < 0.7
                ? ChunkType.TRAPDOOR_FIELD
                : (Math.random() < 0.25f ? ChunkType.OPEN_FIELD : ChunkType.FOREST);
    }

}
