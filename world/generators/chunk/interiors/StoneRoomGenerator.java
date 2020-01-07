package world.generators.chunk.interiors;

import assets.definitions.Tile;
import world.Chunk;
import world.Portal;
import world.World;
import world.generators.chunk.ChunkGenerator;

import java.util.Random;

public class StoneRoomGenerator extends InteriorGenerator {

    private Random rng;

    public StoneRoomGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        if (x >= getMinimum() && x <= getMaximum() && y >= getMinimum() && y <= getMaximum())
            return Tile.STONE_FLOOR;
        else return Tile.AIR;
    }

    @Override
    public byte getTop(int x, int y) {
        if (y == getMinimum()) {
            if (x == Chunk.CHUNK_SIZE/2) return Tile.STONE_WALL_DOOR_NORTH;
            if (x > getMinimum() && x < getMaximum()) return Tile.STONE_WALL_NORTH;
            if (x == getMaximum()) return Tile.STONE_CORNER_NORTHEAST;
            if (x == getMinimum()) return Tile.STONE_CORNER_NORTHWEST;
        }
        if (y > getMinimum() && y < getMaximum()) {
            if (x == getMaximum()) return Tile.STONE_WALL_EAST;
            if (x == getMinimum()) return Tile.STONE_WALL_WEST;
        }
        if (y == getMaximum() && x >= getMinimum() && x <= getMaximum()) {
            return x % 4 != 0 ? (byte)Tile.STONE_WALL_SOUTH : (byte)Tile.STONE_WALL_SOUTH;
        }
        return Tile.AIR;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == getMinimum()
                ? new Portal("door", 0, 1, World.getRegion("world"), "door")
                : null;
    }

}
