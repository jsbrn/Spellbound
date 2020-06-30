package world.generators.chunk.interiors.dungeons;

import com.github.mathiewz.slick.Color;
import world.Tiles;

public class DungeonZombieRoomGenerator extends DungeonRoomGenerator {

    public DungeonZombieRoomGenerator(boolean north, boolean south, boolean east, boolean west, int seed) {
        super(1, north, south, east, west, seed);
    }

    @Override
    public byte getTop(int x, int y) {
        byte original = super.getTop(x, y);
        if (original != Tiles.AIR || !isWithinWalls(x, y)) return original;
        return rng().nextInt(8) == 0
                ? Tiles.BLOOD_STAIN
                : original;
    }

    @Override
    public Color getColor() {
        return Color.green.darker(0.75f);
    }
}
