package world.generators.chunk.interiors.dungeons;

import world.Portal;

public class DungeonKeyRoomGenerator extends DungeonRoomGenerator {

    public DungeonKeyRoomGenerator(boolean north, boolean south, boolean east, boolean west, int seed) {
        super(1, north, south, east, west, seed);
    }

    @Override
    public Portal getPortal() {
        return null;
    }

    @Override
    public String getIcon() {
        return "gui/icons/key.png";
    }

}
