package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.humanoids.Zombie;

import java.util.Random;

public class GraveyardGenerator extends OpenFieldGenerator {

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
                : (Math.random() <= 0.05 ? new Chest(new Random().nextInt(3) + 1, false, Chest.GOLD_LOOT, 0.5f) : null);
    }

    @Override
    public Color getColor() {
        return Color.gray;
    }
}
