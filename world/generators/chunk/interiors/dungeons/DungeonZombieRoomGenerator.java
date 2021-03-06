package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.humanoids.enemies.Zombie;

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
    public Entity getEntity(int x, int y) {
        if (!isWithinWalls(x, y)) return null;
        return rng().nextInt(6) == 0 ? new Zombie() : null;
    }

    @Override
    public Color getColor() {
        return Color.green.darker(0.75f);
    }
}
