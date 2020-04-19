package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.humanoids.npcs.Bandit;

import java.util.Random;

public class DungeonLivingQuartersGenerator extends DungeonRoomGenerator {

    private int difficultyMultiplier;

    public DungeonLivingQuartersGenerator(int difficultyMultiplier, boolean north, boolean south, boolean east, boolean west, int seed) {
        super(1, north, south, east, west, seed);
        this.difficultyMultiplier = difficultyMultiplier;
    }

    @Override
    public byte getTop(int x, int y) {
        byte original = super.getTop(x, y);
        if (original != Tiles.AIR || !isWithinWalls(x, y)) return original;
        return rng().nextInt(8) == 0
                ? (byte)((rng().nextBoolean() ? Tiles.EMPTY_BOTTLES : Tiles.CHAIR))
                : original;
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (!isWithinWalls(x, y)) return null;
        return rng().nextInt(12) == 0 ? new Bandit(difficultyMultiplier) : null;
    }

    @Override
    public Color getColor() {
        return Color.orange.darker();
    }
}
