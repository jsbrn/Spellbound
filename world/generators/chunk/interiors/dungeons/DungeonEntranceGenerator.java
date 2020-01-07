package world.generators.chunk.interiors.dungeons;

import assets.definitions.Tile;
import world.Chunk;
import world.Portal;
import world.World;
import world.generators.chunk.interiors.StoneRoomGenerator;

import java.util.Random;

public class DungeonEntranceGenerator extends StoneRoomGenerator {

    private Random rng;

    public DungeonEntranceGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        int half = Chunk.CHUNK_SIZE / 2;
        if (Math.abs(half-x) <= 2 && y > half) return Tile.STONE_FLOOR; else return super.getBase(x, y);
    }

    @Override
    public byte getTop(int x, int y) {
        int half = Chunk.CHUNK_SIZE / 2;
        if (x == half && y == getMinimum()) return Tile.STONE_LADDER;
        if (x == half && y == getMaximum()) return Tile.AIR;
        if (x == half-1 && y == getMaximum()) return Tile.AIR;
        if (x == half-2 && y == getMaximum()) return Tile.STONE_CORNER_NORTHWEST;
        if (x == half+1 && y == getMaximum()) return Tile.AIR;
        if (x == half+2 && y == getMaximum()) return Tile.STONE_CORNER_NORTHEAST;
        if (y > getMaximum() && x == half - 2) return Tile.STONE_WALL_WEST;
        if (y > getMaximum() && x == half + 2) return Tile.STONE_WALL_EAST;
        return super.getTop(x, y);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == getMinimum()
                ? new Portal("ladder", 0, 2, World.getRegion("world"), "trapdoor")
                : null;
    }

}
