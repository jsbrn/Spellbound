package world.generators.chunk;

import world.Chunk;
import world.Portal;
import world.Region;
import world.World;
import world.generators.region.PlayerHomeRegionGenerator;
import world.generators.region.RegionGenerator;

import java.util.Random;

public class HomeGenerator extends ChunkGenerator {

    private Random rng;
    private int min = 2, max = 10;

    public HomeGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        if (x >= min && x <= max && y >= min && y <= max) return 11; else return 0;
    }

    @Override
    public byte getTop(int x, int y) {
        if (y == min) {
            if (x == Chunk.CHUNK_SIZE/2) return 10;
            if (x > min && x < max) return 9;
            if (x == max) return 12;
            if (x == min) return 15;
        }
        if (y > min && y < max) {
            if (x == max) return 13;
            if (x == min) return 14;
        }
        if (y == max && x >= min && x <= max) {
            return x % 4 != 0 ? (byte)16 : (byte)17;
        }
        return 0;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return x == Chunk.CHUNK_SIZE/2 && y == min
                ? new Portal("door", World.getRegion("world"), "door", 0, 1)
                : null;
    }

}
