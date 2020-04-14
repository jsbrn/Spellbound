package world.generators.chunk.overworld;

import org.newdawn.slick.Color;
import world.*;
import world.generators.region.DungeonGenerator;

import java.util.Random;

public class TrapdoorFieldGenerator extends OpenFieldGenerator {

    private Random rng;
    private boolean placed;
    private int entrance_x, entrance_y;

    public TrapdoorFieldGenerator() {
        this.rng = new Random();
        this.entrance_x = 2 + rng.nextInt(Chunk.CHUNK_SIZE - 4);
        this.entrance_y = 2 + rng.nextInt(Chunk.CHUNK_SIZE - 4);
    }

    @Override
    public byte getTop(int x, int y) {
        return (x == entrance_x && y == entrance_y) ? Tiles.TRAP_DOOR : super.getTop(x, y);
    }

    @Override
    public Portal getPortal(int x, int y) {
        String dungeon_name = "dungeon_"+rng.nextInt();
        return (x == entrance_x && y == entrance_y)
                ? new Portal("trapdoor", 0, 1, false,
                World.addRegion(new Region(dungeon_name, 16, new DungeonGenerator(rng.nextInt(5) + 1, 16))),
                        "ladder")
                : super.getPortal(x, y);
    }

    @Override
    public Color getColor() {
        return Color.orange.darker();
    }
}
