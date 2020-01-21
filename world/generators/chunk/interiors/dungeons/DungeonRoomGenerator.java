package world.generators.chunk.interiors.dungeons;

import assets.definitions.TileType;
import world.Portal;
import world.entities.Entity;
import world.entities.types.humanoids.npcs.Bandit;
import world.entities.types.humanoids.npcs.Civilian;
import world.generators.chunk.interiors.InteriorRoomGenerator;

import java.util.Random;

public class DungeonRoomGenerator extends InteriorRoomGenerator {

    private Random rng;
    private boolean spawnBandits;

    public DungeonRoomGenerator(boolean north, boolean south, boolean east, boolean west) {
        super(north, south, east, west);
        this.rng = new Random();
        this.spawnBandits = rng.nextFloat() < 0.25;
    }

    @Override
    public byte getBase(int x, int y) {
        byte tile = super.getBase(x, y);
        if (tile == TileType.STONE_FLOOR) {
            return (byte)(rng.nextFloat() < 0.2f ? TileType.CRACKED_STONE_FLOOR + rng.nextInt(3) : TileType.STONE_FLOOR);
        }
        return tile;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (x > getMinimum() + 1
                && x < getMaximum() - 2
                && y > getMinimum() + 1
                && y < getMaximum() - 2
                && rng.nextFloat() < 0.2
                && spawnBandits) {
            return new Bandit();
        }
        return null;
    }

}
