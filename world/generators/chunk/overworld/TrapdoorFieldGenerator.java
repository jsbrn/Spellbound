package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.*;
import world.generators.region.DungeonGenerator;

public class TrapdoorFieldGenerator extends OpenFieldGenerator {

    private int entrance_x, entrance_y, difficulty;

    public TrapdoorFieldGenerator(int difficultyMultiplier, int seed) {
        super(seed);
        this.entrance_x = 2 + rng().nextInt(Chunk.CHUNK_SIZE - 4);
        this.entrance_y = 2 + rng().nextInt(Chunk.CHUNK_SIZE - 4);
        this.difficulty = difficultyMultiplier;
    }

    @Override
    public byte getTop(int x, int y) {
        return (x == entrance_x && y == entrance_y) ? Tiles.TRAP_DOOR : super.getTop(x, y);
    }

    @Override
    public Portal getPortal() {
        String dungeon_name = "dungeon_"+ getChunkX()+"_"+ getChunkY();
        Region dungeon = World.addRegion(new Region(dungeon_name, 16, new DungeonGenerator(1 + (difficulty), 16, World.getSeed() + getChunkX() + getChunkY())));
        Portal trapdoor = new Portal("trapdoor", entrance_x, entrance_y, 0, 1, false, dungeon, "ladder");
        return trapdoor;
    }

    @Override
    public Color getColor() {
        return Color.orange.darker();
    }
}
