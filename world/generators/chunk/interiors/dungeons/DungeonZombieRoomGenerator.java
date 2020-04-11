package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.humanoids.Zombie;
import world.entities.types.humanoids.npcs.Bandit;

import java.util.Random;

public class DungeonZombieRoomGenerator extends DungeonRoomGenerator {

    Random rng;

    public DungeonZombieRoomGenerator(boolean north, boolean south, boolean east, boolean west) {
        super(north, south, east, west);
        this.rng = new Random();
    }

    @Override
    public byte getTop(int x, int y) {
        byte original = super.getTop(x, y);
        if (original != Tiles.AIR || !isWithinWalls(x, y)) return original;
        return rng.nextInt(8) == 0
                ? Tiles.BLOOD_STAIN
                : original;
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (!isWithinWalls(x, y)) return null;
        return rng.nextInt(6) == 0 ? new Zombie() : null;
    }

    @Override
    public Color getColor() {
        return Color.green.darker(0.75f);
    }
}
