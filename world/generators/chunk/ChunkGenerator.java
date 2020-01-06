package world.generators.chunk;

import world.Portal;

public abstract class ChunkGenerator {

    public abstract byte getBase(int x, int y);
    public abstract byte getTop(int x, int y);
    public abstract Portal getPortal(int x, int y);

    public static ChunkGenerator get(ChunkType type) {
        if (type == ChunkType.OPEN_FIELD) return new OpenFieldChunkGenerator();
        if (type == ChunkType.FOREST) return new ForestGenerator();
        if (type == ChunkType.BACKYARD_TEST) return new TestBackyardGenerator();
        if (type == ChunkType.HOME) return new HomeGenerator();
        return null;
    }

}
