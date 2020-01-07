package world.generators.chunk.interiors;

import world.Chunk;
import world.generators.chunk.ChunkGenerator;

public abstract class InteriorGenerator extends ChunkGenerator {

    private int min = 2, max = Chunk.CHUNK_SIZE - 3;

    public int getMinimum() { return 2; }
    public int getMaximum() { return Chunk.CHUNK_SIZE - 3; }

    public void setMinimum(int minimum) { this.min = minimum; }
    public void setMaximum(int maximum) { this.min = maximum; }

}
