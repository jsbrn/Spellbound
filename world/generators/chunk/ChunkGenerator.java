package world.generators.chunk;

public abstract class ChunkGenerator {

    public abstract int[][] generateBase(int size);
    public abstract int[][] generateObjects(int size);

    public static ChunkGenerator get(ChunkType type) {
        if (type == ChunkType.OPEN_FIELD) return new OpenFieldChunkGenerator();
        return null;
    }

}
