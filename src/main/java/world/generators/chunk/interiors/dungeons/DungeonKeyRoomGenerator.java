package world.generators.chunk.interiors.dungeons;

import world.Chunk;
import world.Portal;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.humanoids.enemies.Bandit;

public class DungeonKeyRoomGenerator extends DungeonRoomGenerator {

    public DungeonKeyRoomGenerator(boolean north, boolean south, boolean east, boolean west, int seed) {
        super(1, north, south, east, west, seed);
    }

    @Override
    public Portal getPortal() {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == Chunk.CHUNK_SIZE/2
                ? new Chest(0.1f, false, Chest.KEY_LOOT, 1.0f)
                : (isWithinWalls(x, y) && rng().nextInt(3) == 0 ? new Bandit(1) : null);
    }

    @Override
    public String getIcon() {
        return "gui/icons/key.png";
    }

}
