package world.generators.chunk.interiors.dungeons;

import com.github.mathiewz.slick.Color;
import world.Tiles;

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
    public Color getColor() {
        return Color.gray;
    }
}
