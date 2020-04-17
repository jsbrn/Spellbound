package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.WishingWell;
import world.entities.types.humanoids.Zombie;

import java.util.Random;

public class WishingWellFieldGenerator extends OpenFieldGenerator {

    @Override
    public Entity getEntity(int x, int y) {
        return x == Chunk.CHUNK_SIZE / 2 && y == Chunk.CHUNK_SIZE / 2 ? new WishingWell() : null;
    }

    @Override
    public Color getColor() {
        return Color.yellow;
    }
}
