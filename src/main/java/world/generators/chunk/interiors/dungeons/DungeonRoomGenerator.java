package world.generators.chunk.interiors.dungeons;

import com.github.mathiewz.slick.Color;
import world.Portal;
import world.Tiles;
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
    public Color getColor() {
        return Color.darkGray;
    }

    @Override
    public String getIcon() {
        return null;
    }

}
