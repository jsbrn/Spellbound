package world.generators.chunk;

import assets.definitions.TileType;
import org.newdawn.slick.Color;
import world.Portal;
import world.entities.Entity;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    public byte getBase(int x, int y) {
        return TileType.AIR;
    }

    @Override
    public byte getTop(int x, int y) {
        return TileType.AIR;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) { return null; }

    @Override
    public Color getColor() {
        return Color.black;
    }
}
