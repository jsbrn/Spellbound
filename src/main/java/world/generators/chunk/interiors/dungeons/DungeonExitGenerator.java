package world.generators.chunk.interiors.dungeons;

import world.Chunk;
import world.entities.types.Chest;

public class DungeonExitGenerator extends DungeonEntranceGenerator {

    public DungeonExitGenerator(boolean south, boolean east, boolean west, int seed) {
        super(south, east, west, seed);
    }

    @Override
    public Entity getEntity(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == Chunk.CHUNK_SIZE/2
                ? new Chest(10, true, rng().nextBoolean() ? Chest.GOLD_LOOT : Chest.ARTIFACT_LOOT, 1.0f)
                : null;
    }

    @Override
    public String getIcon() {
        return "gui/icons/minimap/door.png";
    }

}
