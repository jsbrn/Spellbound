package world.generators.chunk.interiors.dungeons;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.Chunk;
import world.Portal;
import world.Tiles;
import world.entities.Entity;
import world.entities.types.Chest;
import world.entities.types.SpikeTrap;
import world.entities.types.humanoids.enemies.Bandit;
import world.generators.chunk.interiors.InteriorRoomGenerator;

public class DungeonRoomGenerator extends InteriorRoomGenerator {
    
    private boolean spawnLoot;
    private int chestCount, lootMultiplier;

    public DungeonRoomGenerator(int lootMultiplier, boolean north, boolean south, boolean east, boolean west, int seed) {
        super(north, south, east, west, seed);
        this.setSize(9);
        this.spawnLoot = rng().nextFloat() < 0.75;
        this.lootMultiplier = lootMultiplier;
    }

    @Override
    public byte getBase(int x, int y) {
        byte tile = super.getBase(x, y);
        if (tile == Tiles.STONE_FLOOR) {
            return (byte)(rng().nextFloat() < 0.2f ? Tiles.CRACKED_STONE_FLOOR + rng().nextInt(3) : Tiles.STONE_FLOOR);
        }
        return tile;
    }

    @Override
    public Portal getPortal() {
        return null;
    }

    @Override
    public Entity getEntity(int x, int y) {
        if (isWithinWalls(x, y)
                && rng().nextFloat() < 0.15) {
            return rng().nextInt(10) == 0 ? new Bandit(1) : new SpikeTrap();
        }
        if (isWithinWalls(x, y)
                && spawnLoot && rng().nextFloat() < MiscMath.distance(Chunk.CHUNK_SIZE/2, Chunk.CHUNK_SIZE/2, x, y) / 100
                && chestCount < 3) {
            chestCount++;
            return new Chest(lootMultiplier * 2 / 10f, false, rng().nextInt(3), 0.75f);
        }
        return null;
    }

    @Override
    public Color getColor() {
        return Color.darkGray;
    }

    @Override
    public String getIcon() {
        return null;
    }

}
