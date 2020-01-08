package world.generators.region;

import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.EmptyChunkGenerator;
import world.generators.chunk.interiors.dungeons.DungeonBossRoomGenerator;
import world.generators.chunk.interiors.dungeons.DungeonEntranceGenerator;
import world.generators.chunk.interiors.dungeons.DungeonRoomGenerator;

public class DungeonGenerator implements RegionGenerator {

    private ChunkGenerator[][] map;

    public DungeonGenerator() {

    }

    @Override
    public ChunkGenerator getChunkGenerator(int cx, int cy, int size) {
        if (map == null) map = generateDungeon(size);
        return map[cx][cy];
    }

    private ChunkGenerator[][] generateDungeon(int size) {
        ChunkGenerator[][] generators = new ChunkGenerator[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                generators[i][j] = new EmptyChunkGenerator();
            }
        }
        generators[size/2][0] = new DungeonEntranceGenerator();
        generators[size/2][1] = new DungeonRoomGenerator(true, true, true, true);
        return generators;
    }

}