package world.generators.chunk.interiors.dungeons;

import assets.definitions.Tile;
import world.Portal;
import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.interiors.InteriorRoomGenerator;

import java.util.Random;

public class DungeonRoomGenerator extends InteriorRoomGenerator {

    private Random rng;

    public DungeonRoomGenerator(boolean north, boolean south, boolean east, boolean west) {
        super(north, south, east, west);
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        byte tile = super.getBase(x, y);
        if (tile == Tile.STONE_FLOOR) {
            return (byte)(rng.nextFloat() < 0.2f ? Tile.CRACKED_STONE_FLOOR + rng.nextInt(3) : Tile.STONE_FLOOR);
        }
        return tile;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }

}
