package world.generators.chunk.overworld;

import world.Chunk;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.humanoids.enemies.Zombie;

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
    public Entity getEntity(int x, int y) {
        return Math.random() <= 0.1
                ? new Zombie()
                : (rng().nextFloat() <= 0.05 ? new Chest((rng().nextInt(3) + 1) / 10f, false, Chest.GOLD_LOOT, 0.5f) : null);
    }

    @Override
    public String getIcon() {
        return "assets/gui/icons/minimap/graveyard.png";
    }
}
