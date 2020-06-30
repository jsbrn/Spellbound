package world.generators.chunk.interiors.dungeons;

import gui.sound.SoundManager;
import world.Chunk;
import world.Portal;
import world.Tiles;
import world.World;

public class DungeonEntranceGenerator extends DungeonRoomGenerator {

    public DungeonEntranceGenerator(boolean south, boolean east, boolean west, int seed) {
        super(1, false, south, east, west, seed);
    }

    @Override
    public byte getTop(int x, int y) {
        int half = Chunk.CHUNK_SIZE / 2;
        if (x == half && y == getMinimum()) return Tiles.STONE_LADDER;
        return super.getTop(x, y);
    }

    @Override
    public Portal getPortal() {
        Portal ladder = new Portal("ladder", Chunk.CHUNK_SIZE/2, getMinimum(), 0, 2, true, World.getRegion("world"), "trapdoor", SoundManager.DOOR_OPEN);
        return ladder;
    }

    @Override
    public String getIcon() {
        return "gui/icons/minimap/door.png";
    }
}
