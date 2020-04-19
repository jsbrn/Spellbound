package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Portal;
import world.Tiles;
import world.World;
import world.entities.Entity;

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
    public Portal getPortal(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == getMinimum()
                ? new Portal("ladder", 0, 2, true, World.getRegion("world"), "trapdoor")
                : null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        return null;
    }

    @Override
    public Color getColor() {
        return Color.green;
    }
}
