package world.generators.chunk;

import assets.definitions.Tile;
import world.Chunk;
import world.Portal;
import world.Region;
import world.World;
import world.generators.chunk.overworld.OpenFieldGenerator;
import world.generators.region.DungeonGenerator;

import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    public byte getBase(int x, int y) {
        return Tile.AIR;
    }

    @Override
    public byte getTop(int x, int y) {
        return Tile.AIR;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return null;
    }
}
