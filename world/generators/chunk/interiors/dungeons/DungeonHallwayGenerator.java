package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.SpikeTrap;
import world.entities.types.humanoids.npcs.Bandit;

import java.util.Random;

public class DungeonHallwayGenerator extends DungeonRoomGenerator {

    private Random rng;
    private boolean horizontal;

    public DungeonHallwayGenerator(boolean horizontal) {
        super(1, !horizontal, !horizontal, horizontal, horizontal);
        this.setSize(4);
        this.horizontal = horizontal;
        this.rng = new Random();
    }

    @Override
    public byte getTop(int x, int y) {
        byte original = super.getTop(x, y);
        if (horizontal) {
            if (y == getMinimum()) return Tiles.STONE_WALL_NORTH;
            if (y == getMaximum()) return Tiles.STONE_WALL_NORTH;
        } else {
            if (x == getMinimum()) return Tiles.STONE_WALL_WEST;
            if (x == getMaximum()) return Tiles.STONE_WALL_EAST;
        }
        return original;
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (isWithinWalls(x, y) && rng.nextInt(16) == 0) {
            return rng.nextInt(3) == 0 ? new Bandit(1) : (rng.nextBoolean() ? new SpikeTrap() : new Chest(3, false, rng.nextInt(3), 0.7f));
        }
        return null;
    }

    @Override
    public Color getColor() {
        return Color.gray;
    }
}
