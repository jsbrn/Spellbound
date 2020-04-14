package world.generators.chunk.interiors;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Portal;
import world.Tiles;
import world.World;
import world.entities.Entity;

import java.util.Random;

public class PlayerHomeGenerator extends InteriorRoomGenerator {

    private Random rng;

    public PlayerHomeGenerator() {
        super(false, false, false, false);
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        if (x >= getMinimum() && x <= getMaximum() && y >= getMinimum() && y <= getMaximum()) return 11; else return 0;
    }

    @Override
    public byte getTop(int x, int y) {
        if (y == getMinimum()) {
            if (x == Chunk.CHUNK_SIZE/2) return Tiles.WOOD_WALL_DOOR_NORTH;
            if (x > getMinimum() && x < getMaximum()) return Tiles.WOOD_WALL_NORTH;
            if (x == getMaximum()) return Tiles.WOOD_CORNER_NORTHEAST;
            if (x == getMinimum()) return Tiles.WOOD_CORNER_NORTHWEST;
        }
        if (y > getMinimum() && y < getMaximum()) {
            if (x == getMaximum()) return Tiles.WOOD_WALL_EAST;
            if (x == getMinimum()) return Tiles.WOOD_WALL_WEST;
        }
        if (y == getMaximum() && x >= getMinimum() && x <= getMaximum()) {
            return x % 4 != 0 ? (byte) Tiles.WOOD_WALL_SOUTH : (byte) Tiles.WOOD_WALL_WINDOW_SOUTH;
        }
        if (x == getMinimum() + 2 && y == getMinimum() + 1) return Tiles.FURNACE;
        return Tiles.AIR;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == getMinimum()
                ? new Portal("door", 0, 2, true, World.getRegion("world"), "door")
                : null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        return null;
    }

    @Override
    public Color getColor() {
        return Color.orange;
    }
}
