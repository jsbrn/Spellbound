package world.generators.chunk.overworld;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.Chunk;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.humanoids.enemies.Bandit;

public class BanditCampGenerator extends ForestGenerator {

    public BanditCampGenerator(int seed) {
        super(seed);
    }

    @Override
    public byte getTop(int x, int y) {
        if (y == Chunk.CHUNK_SIZE / 2 && x == Chunk.CHUNK_SIZE / 2) return Tiles.FIRE_PIT;
        if (MiscMath.distance(x, y, Chunk.CHUNK_SIZE/2, Chunk.CHUNK_SIZE/2) < 3 + rng().nextInt(2))
            return rng().nextInt(10) == 0 ? (byte)Tiles.CHAIR : (byte)Tiles.AIR;
        return super.getTop(x, y);
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (x == Chunk.CHUNK_SIZE/2 && y == Chunk.CHUNK_SIZE/2 - 2) return new Chest(0.3f, false, Chest.RANDOM_LOOT, 0.8f);
        if (MiscMath.distance(x, y, Chunk.CHUNK_SIZE/2, Chunk.CHUNK_SIZE/2) < 3 + rng().nextInt(2))
            return rng().nextInt(10) == 0 ? new Bandit(rng().nextInt(3) + 1) : null;
        return super.getEntity(x, y);
    }

    @Override
    public Color getColor() {
        return Color.green;
    }
}
