package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;

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
    public Entity getEntity(int x, int y) {
        if (!isWithinWalls(x, y)) return null;
        return ((y-1) % spacing != 0 && rng().nextInt(12 + rng().nextInt(8)) == 0) ? new Chest(1, false, rng().nextBoolean() ? Chest.TOME_LOOT : Chest.GOLD_LOOT, 0.6f) : null;
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
