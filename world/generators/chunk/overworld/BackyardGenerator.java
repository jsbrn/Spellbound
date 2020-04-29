package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.Region;
import world.Portal;
import world.World;
import world.generators.region.PlayerHomeRegionGenerator;

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
    public Portal getPortal() {
        Portal door = new Portal("door", 6, 5, 0, 1, true,
                    World.addRegion(new Region("player_home", 1, new PlayerHomeRegionGenerator(World.getSeed()))), "door");
        door.setCoordinates(6, 5);
        return door;
    }

    @Override
    public String getIcon() {
        return "assets/gui/icons/minimap/home.png";
    }
}
