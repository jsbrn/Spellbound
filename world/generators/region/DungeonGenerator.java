package world.generators.region;

import world.Chunk;
import world.generators.chunk.ChunkType;

public class DungeonGenerator implements RegionGenerator {

    public DungeonGenerator() {

    }

    @Override
    public ChunkType getChunkType(int cx, int cy, int size) {
        if (cx == size/2) {
            if (cy == 0) return ChunkType.DUNGEON_ENTRANCE;
            if (cy == 3) return ChunkType.DUNGEON_SOUTH_ROOM;
            return ChunkType.DUNGEON_HALLWAY_NS;
        }
        return ChunkType.EMPTY;
    }

}