package world.generators.chunk.overworld;

import world.Chunk;
import world.Tiles;

public class GraveyardGenerator extends OpenFieldGenerator {

    public GraveyardGenerator(int seed) {
        super(seed);
    }

    @Override
    public byte getTop(int x, int y) {
        if (x > 1 && y > 1 && x <= Chunk.CHUNK_SIZE - 1 && y <= Chunk.CHUNK_SIZE - 1 && Math.random() <= 0.1)
            return Tiles.TOMBSTONE;
        return super.getTop(x, y);
    }

    @Override
    public String getIcon() {
        return "gui/icons/minimap/graveyard.png";
    }
}
