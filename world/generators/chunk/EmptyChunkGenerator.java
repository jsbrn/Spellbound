package world.generators.chunk;

import org.newdawn.slick.Color;
import world.Portal;
import world.Tiles;
import world.entities.Entity;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    public byte getBase(int x, int y) {
        return Tiles.AIR;
    }

    @Override
    public byte getTop(int x, int y) {
        return Tiles.AIR;
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
