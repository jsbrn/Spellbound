package world.generators.world;

import world.generators.chunk.ChunkType;

public class DefaultWorldGenerator implements WorldGenerator {

    public ChunkType[][] generateChunkMap(int size) {
        ChunkType[][] map = new ChunkType[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = ChunkType.OPEN_FIELD;
            }
        }
        return map;
    }

}
