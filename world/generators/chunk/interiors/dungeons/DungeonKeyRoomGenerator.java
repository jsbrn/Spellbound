package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Portal;
import world.Tiles;
import world.World;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.humanoids.npcs.Bandit;

import java.util.Random;

public class DungeonKeyRoomGenerator extends DungeonRoomGenerator {

    public DungeonKeyRoomGenerator(boolean north, boolean south, boolean east, boolean west) {
        super(north, south, east, west);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == Chunk.CHUNK_SIZE/2
                ? new Chest(1, false, Chest.KEY_LOOT, 1.0f)
                : (isWithinWalls(x, y) && new Random().nextBoolean() ? new Bandit() : null);
    }

    @Override
    public Color getColor() {
        return Color.yellow;
    }
}
