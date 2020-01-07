package world.generators.chunk.interiors;

import world.Chunk;
import world.generators.chunk.ChunkGenerator;

public abstract class InteriorGenerator extends ChunkGenerator {

    public int getMinimum() { return 2; }
    public int getMaximum() { return Chunk.CHUNK_SIZE - 3; }

}
