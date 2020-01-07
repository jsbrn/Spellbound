package world.generators.chunk.interiors.dungeons;

import assets.definitions.Tile;
import world.Chunk;
import world.Portal;
import world.generators.chunk.interiors.StoneRoomGenerator;

import java.util.Random;

public class DungeonBossRoomGenerator extends DungeonRoomGenerator {

    private Random rng;

    public DungeonBossRoomGenerator(boolean north, boolean south, boolean east, boolean west) {
        super(north, south, east, west);
        this.setMaximum(Chunk.CHUNK_SIZE - 1);
        this.setMinimum(0);
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        return super.getBase(x, y);
    }

    @Override
    public byte getTop(int x, int y) {
        return super.getTop(x, y);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }

}
