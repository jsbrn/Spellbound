package world.generators.chunk;

import world.Portal;
import world.entities.Entity;

public abstract class ChunkGenerator {

    public abstract byte getBase(int x, int y);
    public abstract byte getTop(int x, int y);
    public abstract Portal getPortal(int x, int y);
    public abstract Entity getEntity(int x, int y);

}
