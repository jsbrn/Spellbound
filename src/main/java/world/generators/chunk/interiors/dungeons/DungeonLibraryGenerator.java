package world.generators.chunk.interiors.dungeons;

import com.github.mathiewz.slick.Color;
import world.Tiles;

public class DungeonLibraryGenerator extends DungeonRoomGenerator {

    private int spacing;

    public DungeonLibraryGenerator(boolean north, boolean south, boolean east, boolean west, int seed) {
        super(1, north, south, east, west, seed);
        this.setSize(10);
        this.spacing = 3 + rng().nextInt(4);
    }

    @Override
    public byte getTop(int x, int y) {
        byte original = super.getTop(x, y);
        if (original != Tiles.AIR || !isWithinWalls(x, y)) return original;

        return (x < getMaximum()/2 || x > 2 + getMaximum()/2) && (y-1) % spacing == 0 ? Tiles.BOOKSHELF_NORTH : original;
    }

    @Override
    public Color getColor() {
        return Color.orange;
    }

    @Override
    public String getIcon() {
        return "gui/icons/tome.png";
    }
}
