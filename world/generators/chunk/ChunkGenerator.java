package world.generators.chunk;

import world.Portal;
import world.generators.chunk.interiors.WoodRoomGenerator;
import world.generators.chunk.interiors.dungeons.DungeonBossRoomGenerator;
import world.generators.chunk.interiors.dungeons.DungeonEntranceGenerator;
import world.generators.chunk.interiors.dungeons.DungeonRoomGenerator;
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
        if (type == ChunkType.DUNGEON_ENTRANCE) return new DungeonEntranceGenerator(false, true, false, false);
        if (type == ChunkType.DUNGEON_JUNCTION) return new DungeonRoomGenerator(true, true, true, true);
        if (type == ChunkType.DUNGEON_HALLWAY_NS) return new DungeonRoomGenerator(true, true, false, false);
        if (type == ChunkType.DUNGEON_HALLWAY_EW) return new DungeonRoomGenerator(false, false, true, true);
        if (type == ChunkType.DUNGEON_NORTH_ROOM) return new DungeonRoomGenerator(false, true, false, false);
        if (type == ChunkType.DUNGEON_SOUTH_ROOM) return new DungeonRoomGenerator(true, false, false, false);
        if (type == ChunkType.DUNGEON_EAST_ROOM) return new DungeonRoomGenerator(false, false, false, true);
        if (type == ChunkType.DUNGEON_WEST_ROOM) return new DungeonRoomGenerator(false, true, true, false);
        if (type == ChunkType.DUNGEON_BOSS_ROOM) return new DungeonBossRoomGenerator(false, true, true, false);
        if (type == ChunkType.EMPTY) return new EmptyChunkGenerator();
        return null;
    }

}
