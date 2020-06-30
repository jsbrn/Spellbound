package world.generators.chunk.interiors.dungeons;

import com.github.mathiewz.slick.Color;
import world.Tiles;

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
    public Color getColor() {
        return Color.orange.darker();
    }
}
