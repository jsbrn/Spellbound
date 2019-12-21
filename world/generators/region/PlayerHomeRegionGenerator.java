package world.generators.region;

import world.generators.chunk.ChunkType;

public class PlayerHomeRegionGenerator implements RegionGenerator {

    public ChunkType[][] generateChunkMap(int size) {
        ChunkType[][] map = new ChunkType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = ChunkType.HOME;
            }
        }
        return map;
    }

}
