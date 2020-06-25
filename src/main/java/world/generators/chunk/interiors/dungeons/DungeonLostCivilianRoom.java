package world.generators.chunk.interiors.dungeons;

import world.entities.types.humanoids.npcs.LostCivilian;

public class DungeonLostCivilianRoom extends DungeonLivingQuartersGenerator {
    
    boolean spawnedCivilian;
    int difficultyMultiplier;

    public DungeonLostCivilianRoom(int difficultyMultiplier, boolean north, boolean south, boolean east, boolean west, int seed) {
        super(1, north, south, east, west, seed);
        this.difficultyMultiplier = difficultyMultiplier;
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (!isWithinWalls(x, y)) return null;
        Entity e = rng().nextInt(6) == 0 && !spawnedCivilian ? new LostCivilian(difficultyMultiplier) : null;
        if (e != null) spawnedCivilian = true;
        return e;
    }

    @Override
    public String getIcon() {
        return "gui/icons/minimap/exclamation.png";
    }

}
