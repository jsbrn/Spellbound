package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;

import java.util.Random;

public class DungeonLibraryGenerator extends DungeonRoomGenerator {

    private int spacing;
    Random rng;

    public DungeonLibraryGenerator(boolean north, boolean south, boolean east, boolean west) {
        super(north, south, east, west);
        this.setSize(10);
        this.rng = new Random();
        this.spacing = 3 + rng.nextInt(4);
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
        return ((y-1) % spacing != 0 && rng.nextInt(12 + rng.nextInt(8)) == 0) ? new Chest(rng.nextInt(5) + 1) : null;
    }

    @Override
    public Color getColor() {
        return Color.orange;
    }
}
