package world.generators.chunk;

import world.RegionLink;

public abstract class ChunkGenerator {

    public abstract byte[][] generateBase(int size);
    public abstract byte[][] generateObjects(int size);
    public abstract RegionLink[][] generateLinks(int size);

    public static ChunkGenerator get(ChunkType type) {
        if (type == ChunkType.OPEN_FIELD) return new OpenFieldChunkGenerator();
        if (type == ChunkType.FOREST) return new ForestGenerator();
        if (type == ChunkType.BACKYARD_TEST) return new TestBackyardGenerator();
        if (type == ChunkType.HOME) return new HomeGenerator();
        return null;
    }

}
