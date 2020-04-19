package world.generators.chunk.interiors.dungeons;

import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.humanoids.npcs.Bandit;
import world.entities.types.humanoids.npcs.LostCivilian;

import java.util.Random;

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
    public Color getColor() {
        return Color.pink.darker();
    }
}
