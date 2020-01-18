package world.generators.chunk.interiors.dungeons;

import world.Chunk;
import world.Portal;

public class DungeonBossRoomGenerator extends DungeonRoomGenerator {

    public DungeonBossRoomGenerator() {
        super(false, true, false, false);
        this.setSize(Chunk.CHUNK_SIZE);
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