package world.generators.chunk;

import world.Region;
import world.Portal;
import world.World;
import world.generators.region.PlayerHomeRegionGenerator;

import java.util.Random;

public class TestBackyardGenerator extends OpenFieldChunkGenerator {

    private Random rng;

    public TestBackyardGenerator() {
        this.rng = new Random();
    }

    @Override
    public byte getTop(int x, int y) {
        if (y == 5) {
            if (x >= 5 && x <= 7) return (byte)x;
        }
        return super.getTop(x, y);
    }

    @Override
    public Portal getPortal(int x, int y) {
        return (x == 6 && y == 5)
                ? new Portal("door", World.addRegion(new Region("player_home", 1, new PlayerHomeRegionGenerator())), "door", 0, 1)
                : null;
    }
}
