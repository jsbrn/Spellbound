package world.generators.world;

import world.generators.chunk.ChunkType;

public interface WorldGenerator {

    ChunkType[][] generateChunkMap(int size);

}
