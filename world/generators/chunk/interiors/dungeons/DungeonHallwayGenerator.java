package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.SpikeTrap;
import world.entities.types.humanoids.enemies.Bandit;

public class DungeonHallwayGenerator extends DungeonRoomGenerator {

    private boolean horizontal;

    public DungeonHallwayGenerator(boolean horizontal, int seed) {
        super(1, !horizontal, !horizontal, horizontal, horizontal, seed);
        this.setSize(4);
        this.horizontal = horizontal;
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
        if (isWithinWalls(x, y) && rng().nextInt(8) == 0) {
            return rng().nextInt(3) == 0 ? new Bandit(1) : (rng().nextBoolean() ? new SpikeTrap() : new Chest(0.3f, false, rng().nextInt(3), 0.7f));
        }
        return null;
    }

    @Override
    public Color getColor() {
        return Color.gray;
    }
}
