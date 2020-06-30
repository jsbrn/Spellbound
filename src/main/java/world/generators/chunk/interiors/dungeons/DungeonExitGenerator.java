package world.generators.chunk.interiors.dungeons;

public class DungeonExitGenerator extends DungeonEntranceGenerator {

    public DungeonExitGenerator(boolean south, boolean east, boolean west, int seed) {
        super(south, east, west, seed);
    }

    @Override
    public String getIcon() {
        return "gui/icons/minimap/door.png";
    }

}
