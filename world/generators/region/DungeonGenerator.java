package world.generators.region;

import world.Chunk;
import world.generators.chunk.ChunkType;

public class DungeonGenerator implements RegionGenerator {

    public DungeonGenerator() {

    }

    @Override
    public ChunkType getChunkType(int cx, int cy, int size) {
        if (cx == Chunk.CHUNK_SIZE/2) {
            if (cy == 0) return ChunkType.DUNGEON_ENTRANCE;
            return ChunkType.DUNGEON_NS_HALLWAY;
        }
        return ChunkType.EMPTY;
    }

}
