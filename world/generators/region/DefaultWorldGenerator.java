package world.generators.region;

import world.generators.chunk.ChunkType;

public class DefaultWorldGenerator implements RegionGenerator {

    public ChunkType[][] generateChunkMap(int size) {
        ChunkType[][] map = new ChunkType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = Math.random() < 0.25f ? ChunkType.OPEN_FIELD : ChunkType.FOREST;
            }
        }
        map[1][1] = ChunkType.BACKYARD_TEST;
        return map;
    }

}
