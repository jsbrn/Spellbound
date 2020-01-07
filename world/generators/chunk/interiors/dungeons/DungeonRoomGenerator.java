package world.generators.chunk.interiors.dungeons;

import assets.definitions.Tile;
import world.Chunk;
import world.Portal;
import world.generators.chunk.interiors.StoneRoomGenerator;

import java.util.Random;

public class DungeonRoomGenerator extends StoneRoomGenerator {

    private Random rng;
    private boolean north, south, east, west;

    public DungeonRoomGenerator(boolean north, boolean south, boolean east, boolean west) {
        this.rng = new Random();
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    @Override
    public byte getBase(int x, int y) {
        int half = Chunk.CHUNK_SIZE / 2;
        byte floor = (byte)(rng.nextFloat() < 0.2f ? Tile.CRACKED_STONE_FLOOR + rng.nextInt(3) : Tile.STONE_FLOOR);
        if (north)
            if (Math.abs(half-x) <= 2 && y < getMinimum()) return floor;
        if (south)
            if (Math.abs(half-x) <= 2 && y > getMaximum()) return floor;
        if (east)
            if (Math.abs(half-y) <= 2 && x > getMaximum()) return floor;
        if (west)
            if (Math.abs(half-y) <= 2 && x < getMinimum()) return floor;
        if (super.getBase(x, y) != Tile.AIR) return floor; else return Tile.AIR;
    }

    @Override
    public byte getTop(int x, int y) {
        
        int half = Chunk.CHUNK_SIZE / 2;
        //north entrance
        if (north && y <= getMinimum()) {
            if (Math.abs(half-x) <= 1) return Tile.AIR;
            if (x == half - 2) return Tile.STONE_WALL_WEST;
            if (x == half + 2) return Tile.STONE_WALL_EAST;
        }
        if (south && y >= getMaximum()) {
            if (Math.abs(half-x) <= 1) return Tile.AIR;
            if (x == half-2 && y == getMaximum()) return Tile.STONE_CORNER_NORTHWEST;
            if (x == half+2 && y == getMaximum()) return Tile.STONE_CORNER_NORTHEAST;
            if (y > getMaximum() && x == half - 2) return Tile.STONE_WALL_WEST;
            if (y > getMaximum() && x == half + 2) return Tile.STONE_WALL_EAST;
        }
        if (east && x >= getMaximum()) {
            if (Math.abs(half-y) <= 1) return Tile.AIR;
            if (x > getMaximum() && y == half - 2) return Tile.STONE_WALL_NORTH;
            if (x == getMaximum() && (y == half - 1 || y == half + 1)) return Tile.STONE_WALL_EAST;
            if (x == getMaximum() && y == half + 2) return Tile.STONE_COLUMN_EAST;
            if (x > getMaximum() && y == half + 2) return Tile.STONE_WALL_SOUTH;
        }
        if (west && x <= getMinimum()) {
            if (Math.abs(half-y) <= 1) return Tile.AIR;
            if (x < getMinimum() && y == half - 2) return Tile.STONE_WALL_NORTH;
            if (x == getMinimum() && (y == half - 1 || y == half + 1)) return Tile.STONE_WALL_WEST;
            if (x == getMinimum() && y == half + 2) return Tile.STONE_COLUMN_WEST;
            if (x < getMinimum() && y == half + 2) return Tile.STONE_WALL_SOUTH;
        }

        return super.getTop(x, y);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }

}
