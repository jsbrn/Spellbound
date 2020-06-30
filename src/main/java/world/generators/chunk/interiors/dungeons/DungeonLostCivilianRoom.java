package world.generators.chunk.interiors.dungeons;

public class DungeonLostCivilianRoom extends DungeonLivingQuartersGenerator {
    
    boolean spawnedCivilian;
    int difficultyMultiplier;

    public DungeonLostCivilianRoom(int difficultyMultiplier, boolean north, boolean south, boolean east, boolean west, int seed) {
        super(1, north, south, east, west, seed);
        this.difficultyMultiplier = difficultyMultiplier;
    }

    @Override
    public String getIcon() {
        return "gui/icons/minimap/exclamation.png";
    }

}
