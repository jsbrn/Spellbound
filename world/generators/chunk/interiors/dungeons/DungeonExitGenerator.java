package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Portal;
import world.Tiles;
import world.World;
import world.entities.Entity;

public class DungeonExitGenerator extends DungeonRoomGenerator {

    public DungeonExitGenerator(boolean south, boolean east, boolean west) {
        super(false, south, east, west);
    }

    @Override
    public byte getTop(int x, int y) {
        int half = Chunk.CHUNK_SIZE / 2;
        if (x == half && y == getMinimum()) return Tiles.BROKEN_STONE_WALL;
        return super.getTop(x, y);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == getMinimum()
                ? new Portal("hole", 0, 2, true, World.getRegion("world"), "trapdoor")
                : null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        return null;
    }

    @Override
    public Color getColor() {
        return Color.red;
    }
}
