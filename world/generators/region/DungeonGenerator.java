package world.generators.region;

import misc.MiscMath;
import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.EmptyChunkGenerator;
import world.generators.chunk.interiors.dungeons.DungeonBossRoomGenerator;
import world.generators.chunk.interiors.dungeons.DungeonEntranceGenerator;
import world.generators.chunk.interiors.dungeons.DungeonRoomGenerator;

import java.util.Random;

public class DungeonGenerator implements RegionGenerator {

    private Random rng;
    private ChunkGenerator[][] map;

    public DungeonGenerator() {
        this.rng = new Random();
    }

    @Override
    public ChunkGenerator getChunkGenerator(int cx, int cy, int size) {
        if (map == null) generateDungeon((int)MiscMath.clamp(size, 6, Integer.MAX_VALUE));
        return map[cx][cy];
    }

    private void generateDungeon(int size) {
        map = new ChunkGenerator[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new EmptyChunkGenerator();
            }
        }

        map[size/2][size-1] = new DungeonEntranceGenerator(false, true, true);

        map[size/2 - 1][size-1] = new DungeonRoomGenerator(true, false, true, false);
        map[size/2 + 1][size-1] = new DungeonRoomGenerator(true, false, false, true);

        map[size/2 - 1][size-2] = new DungeonRoomGenerator(false, true, true, false);
        map[size/2 + 1][size-2] = new DungeonRoomGenerator(false, true, false, true);
        map[size/2][size-2] = new DungeonRoomGenerator(true, false, true, true);

        for (int j = 0; j < size - 2; j++) {
            boolean branch = j > 0 && rng.nextFloat() < .4f;
            if (branch) buildRow(j, map);
            map[size/2][j] = new DungeonRoomGenerator(true, true, branch, branch);
        }

        for (int j = 1; j < size; j++)

        map[size/2][0] = new DungeonBossRoomGenerator();
    }

    private void buildRow(int j, ChunkGenerator[][] map) {
        int size = map[0].length;
        int length = (int)MiscMath.clamp(rng.nextInt(size/2), 1, 4);
        int min = (size / 2) - length;
        int max = (size / 2) + length;
        for (int i = min; i <= max; i++)
            map[i][j] = new DungeonRoomGenerator(false, false, i != max, i != min);
    }

}