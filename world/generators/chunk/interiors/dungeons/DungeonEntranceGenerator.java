package world.generators.chunk.interiors.dungeons;

import assets.definitions.Tile;
import world.Chunk;
import world.Portal;
import world.World;

import java.util.Random;

public class DungeonEntranceGenerator extends DungeonRoomGenerator {

    public DungeonEntranceGenerator(boolean south, boolean east, boolean west) {
        super(false, south, east, west);
    }

    @Override
    public byte getTop(int x, int y) {
        int half = Chunk.CHUNK_SIZE / 2;
        if (x == half && y == getMinimum()) return Tile.STONE_LADDER;
        return super.getTop(x, y);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == getMinimum()
                ? new Portal("ladder", 0, 2, true, World.getRegion("world"), "trapdoor")
                : null;
    }

}
