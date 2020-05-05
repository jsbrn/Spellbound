package world.generators.chunk.overworld;

import world.Chunk;
import world.entities.Entity;
import world.entities.types.WishingWell;

public class WishingWellFieldGenerator extends OpenFieldGenerator {

    public WishingWellFieldGenerator(int seed) {
        super(seed);
    }

    @Override
    public Entity getEntity(int x, int y) {
        return x == Chunk.CHUNK_SIZE / 2 && y == Chunk.CHUNK_SIZE / 2 ? new WishingWell() : null;
    }

    @Override
    public String getIcon() {
        return "assets/animations/wishing_well.png";
    }
}
