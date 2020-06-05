package world.generators.chunk.interiors;

import world.Chunk;
import world.Tiles;
import world.generators.chunk.ChunkGenerator;

import java.util.Random;

public abstract class InteriorRoomGenerator extends ChunkGenerator {

    private int min = 2, max = Chunk.CHUNK_SIZE - 3;
    private boolean north, south, east, west;

    public InteriorRoomGenerator(boolean north, boolean south, boolean east, boolean west, int seed) {
        super(seed);
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    public int getMinimum() { return min; }
    public int getMaximum() { return max; }

    public boolean isWithinWalls(int x, int y) {
        return x > getMinimum() && x < getMaximum() && y > getMinimum() && y < getMaximum();
    }

    public void setSize(int size) {
        int remainder = Chunk.CHUNK_SIZE - size;
        min = remainder / 2;
        max = Chunk.CHUNK_SIZE - 1 - (remainder / 2);
    }

    @Override
    public byte getBase(int x, int y) {
        int half = Chunk.CHUNK_SIZE / 2;

        byte floor = Tiles.STONE_FLOOR;

        if (north)
            if (Math.abs(half-x) <= 2 && y < getMinimum()) return floor;
        if (south)
            if (Math.abs(half-x) <= 2 && y > getMaximum()) return floor;
        if (east)
            if (Math.abs(half-y) <= 2 && x > getMaximum()) return floor;
        if (west)
            if (Math.abs(half-y) <= 2 && x < getMinimum()) return floor;

        if (x >= getMinimum() && x <= getMaximum() && y >= getMinimum() && y <= getMaximum())
            return floor;
        else return Tiles.AIR;

    }

    @Override
    public byte getTop(int x, int y) {

        int half = Chunk.CHUNK_SIZE / 2;
        //north entrance
        if (north && y <= getMinimum()) {
            if (Math.abs(half-x) <= 1) return Tiles.AIR;
            if (x == half - 2) return Tiles.STONE_WALL_WEST;
            if (x == half + 2) return Tiles.STONE_WALL_EAST;
        }
        if (south && y >= getMaximum()) {
            if (Math.abs(half-x) <= 1) return Tiles.AIR;
            if (x == half-2 && y == getMaximum()) return Tiles.STONE_COLUMN_WEST;
            if (x == half+2 && y == getMaximum()) return Tiles.STONE_COLUMN_EAST;
            if (y > getMaximum() && x == half - 2) return Tiles.STONE_WALL_WEST;
            if (y > getMaximum() && x == half + 2) return Tiles.STONE_WALL_EAST;
        }
        if (east && x >= getMaximum()) {
            if (Math.abs(half-y) <= 1) return Tiles.AIR;
            if (x > getMaximum() && y == half - 2) return Tiles.STONE_WALL_NORTH;
            if (x == getMaximum() && (y == half - 1 || y == half + 1)) return Tiles.STONE_WALL_EAST;
            if (x == getMaximum() && y == half + 2) return Tiles.STONE_COLUMN_EAST;
            if (x > getMaximum() && y == half + 2) return Tiles.STONE_WALL_SOUTH;
        }
        if (west && x <= getMinimum()) {
            if (Math.abs(half-y) <= 1) return Tiles.AIR;
            if (x < getMinimum() && y == half - 2) return Tiles.STONE_WALL_NORTH;
            if (x == getMinimum() && (y == half - 1 || y == half + 1)) return Tiles.STONE_WALL_WEST;
            if (x == getMinimum() && y == half + 2) return Tiles.STONE_COLUMN_WEST;
            if (x < getMinimum() && y == half + 2) return Tiles.STONE_WALL_SOUTH;
        }

        if (x == getMinimum() && y > getMinimum() && y < getMaximum()) return Tiles.STONE_WALL_WEST;
        if (x == getMaximum() && y > getMinimum() && y < getMaximum()) return Tiles.STONE_WALL_EAST;
        if (y == getMinimum()) {
            if (x > getMinimum() && x < getMaximum()) return Tiles.STONE_WALL_NORTH;
            if (x == getMinimum()) return Tiles.STONE_CORNER_NORTHWEST;
            if (x == getMaximum()) return Tiles.STONE_CORNER_NORTHEAST;
        }
        if (y == getMaximum()) {
            if (x >= getMinimum() && x <= getMaximum()) return Tiles.STONE_WALL_SOUTH;
        }

        return Tiles.AIR;
    }

}
