package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Chunk;
import world.Portal;

public class DungeonBossRoomGenerator extends DungeonRoomGenerator {

    public DungeonBossRoomGenerator() {
        super(1, false, true, false, false);
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

    @Override
    public Color getColor() {
        return Color.darkGray;
    }
}
