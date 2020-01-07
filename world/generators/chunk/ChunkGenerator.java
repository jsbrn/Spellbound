package world.generators.chunk;

import world.Portal;
import world.generators.chunk.interiors.WoodRoomGenerator;
import world.generators.chunk.interiors.dungeons.DungeonEntranceGenerator;
import world.generators.chunk.interiors.dungeons.DungeonNSHallwayGenerator;
import world.generators.chunk.overworld.BackyardGenerator;
import world.generators.chunk.overworld.TrapdoorFieldGenerator;
import world.generators.chunk.overworld.ForestGenerator;
import world.generators.chunk.overworld.OpenFieldGenerator;

public abstract class ChunkGenerator {

    public abstract byte getBase(int x, int y);
    public abstract byte getTop(int x, int y);
    public abstract Portal getPortal(int x, int y);

    public static ChunkGenerator get(ChunkType type) {
        if (type == ChunkType.OPEN_FIELD) return new OpenFieldGenerator();
        if (type == ChunkType.FOREST) return new ForestGenerator();
        if (type == ChunkType.BACKYARD) return new BackyardGenerator();
        if (type == ChunkType.HOME) return new WoodRoomGenerator();
        if (type == ChunkType.TRAPDOOR_FIELD) return new TrapdoorFieldGenerator();
        if (type == ChunkType.DUNGEON_ENTRANCE) return new DungeonEntranceGenerator();
        if (type == ChunkType.DUNGEON_NS_HALLWAY) return new DungeonNSHallwayGenerator();
        if (type == ChunkType.EMPTY) return new EmptyChunkGenerator();
        return null;
    }

}
