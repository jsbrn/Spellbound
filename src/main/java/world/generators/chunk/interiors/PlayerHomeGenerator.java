package world.generators.chunk.interiors;

import com.github.mathiewz.slick.Color;
import gui.sound.SoundManager;
import network.MPServer;
import world.Chunk;
import world.Portal;
import world.Tiles;
import world.World;

public class PlayerHomeGenerator extends InteriorRoomGenerator {

    public PlayerHomeGenerator(int seed) {
        super(false, false, false, false, seed);
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
        if (x == getMinimum() + 2 && y == getMinimum() + 4) return Tiles.CHAIR;
        if (x == getMinimum() + 3 && y == getMinimum() + 4) return Tiles.CHAIR;
        if (x == getMaximum() - 2 && y == getMinimum() + 3) return Tiles.EMPTY_BOTTLES;
        if (x == getMaximum() - 3 && y == getMinimum() + 5) return Tiles.EMPTY_BOTTLES;
        return Tiles.AIR;
    }

    @Override
    public Portal getPortal() {
        Portal door = new Portal("door", Chunk.CHUNK_SIZE/2, getMinimum(), 0, 2, true, MPServer.getWorld().getRegion("world"), "door", SoundManager.DOOR_OPEN);
        return door;
    }

    @Override
    public Color getColor() {
        return Color.orange;
    }

    @Override
    public String getIcon() {
        return null;
    }

}
