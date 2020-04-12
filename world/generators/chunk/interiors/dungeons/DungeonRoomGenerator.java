package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Portal;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.SpikeTrap;
import world.entities.types.humanoids.npcs.Bandit;
import world.generators.chunk.interiors.InteriorRoomGenerator;

import java.util.Random;

public class DungeonRoomGenerator extends InteriorRoomGenerator {

    private Random rng;
    private boolean spawnLoot;
    private int chestCount;

    public DungeonRoomGenerator(boolean north, boolean south, boolean east, boolean west) {
        super(north, south, east, west);
        this.setSize(9);
        this.rng = new Random();
        this.spawnLoot = rng.nextFloat() < 0.75;
    }

    @Override
    public byte getBase(int x, int y) {
        byte tile = super.getBase(x, y);
        if (tile == Tiles.STONE_FLOOR) {
            return (byte)(rng.nextFloat() < 0.2f ? Tiles.CRACKED_STONE_FLOOR + rng.nextInt(3) : Tiles.STONE_FLOOR);
        }
        return tile;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (isWithinWalls(x, y)
                && rng.nextFloat() < 0.15) {
            return rng.nextInt(10) == 0 ? new Bandit() : new SpikeTrap();
        }
        if (isWithinWalls(x, y)
                && spawnLoot && rng.nextFloat() < 0.1
                && chestCount < 3) {
            chestCount++;
            return new Chest(rng.nextInt(4), false, rng.nextInt(3), 0.5f);
        }
        return null;
    }

    @Override
    public Color getColor() {
        return Color.darkGray;
    }
}
