package world.generators.chunk;

import world.Portal;
import world.Region;
import world.World;
import world.generators.region.PlayerHomeRegionGenerator;
import world.generators.region.RegionGenerator;

import java.util.Random;

public class HomeGenerator extends ChunkGenerator {

    private Random rng;

    public HomeGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getBase(int x, int y) {
        return 1;
    }

    @Override
    public byte getTop(int x, int y) {
        return 0;
    }

    @Override
    public Portal getPortal(int x, int y) {
        return x == 1 && y == 1
                ? new Portal("door", World.getRegion("world"), "door", 0, 1)
                : null;
    }

}
