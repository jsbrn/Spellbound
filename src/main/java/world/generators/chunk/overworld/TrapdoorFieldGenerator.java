package world.generators.chunk.overworld;

import com.github.mathiewz.slick.Color;
import gui.sound.SoundManager;
import network.MPServer;
import world.*;
import world.generators.region.DungeonGenerator;

public class TrapdoorFieldGenerator extends OpenFieldGenerator {

    private int entrance_x, entrance_y, difficulty;
    private boolean civilian;

    public TrapdoorFieldGenerator(int difficultyMultiplier, int seed) {
        super(seed);
        this.entrance_x = 2 + rng().nextInt(Chunk.CHUNK_SIZE - 4);
        this.entrance_y = 2 + rng().nextInt(Chunk.CHUNK_SIZE - 4);
        this.difficulty = difficultyMultiplier;
        this.civilian = rng().nextInt(10) == 0;
    }

    @Override
    public byte getTop(int x, int y) {
        return (x == entrance_x && y == entrance_y) ? Tiles.TRAP_DOOR : super.getTop(x, y);
    }

    @Override
    public Portal getPortal() {
        String dungeon_name = "dungeon_"+ getChunkX()+"_"+ getChunkY();
        Region dungeon = MPServer.getWorld().addRegion(new Region(dungeon_name, 16, new DungeonGenerator(1 + (difficulty), 16,
                MPServer.getWorld().getSeed() + getChunkX() + getChunkY())));
        Portal trapdoor = new Portal("trapdoor", entrance_x, entrance_y, 0, 1, false, dungeon, "ladder", SoundManager.DOOR_OPEN);
        return trapdoor;
    }

    @Override
    public Color getColor() {
        return Color.orange.darker();
    }

    @Override
    public String getIcon() {
        return "gui/icons/minimap/trapdoor.png";
    }
}
