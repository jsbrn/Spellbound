package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.Region;
import world.Portal;
import world.World;
import world.generators.region.PlayerHomeRegionGenerator;

import java.util.Random;

public class BackyardGenerator extends OpenFieldGenerator {

    public BackyardGenerator(int seed) {
        super(seed);
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
                ? new Portal("door", 0, 1, true, World.getRegion("player_home"), "door")
                : null;
    }

    @Override
    public Color getColor() {
        return Color.orange;
    }
}
