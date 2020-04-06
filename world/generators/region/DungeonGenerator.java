package world.generators.region;

import misc.MiscMath;
import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.EmptyChunkGenerator;
import world.generators.chunk.interiors.dungeons.DungeonBossRoomGenerator;
import world.generators.chunk.interiors.dungeons.DungeonEntranceGenerator;
import world.generators.chunk.interiors.dungeons.DungeonRoomGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

public class DungeonGenerator implements RegionGenerator {

    private Random rng;
    private ChunkGenerator[][] map;
    private int[] cursor;

    private int maxBranchLength, segmentCount;

    public DungeonGenerator(int segmentCount, int maxBranchLength) {
        this.rng = new Random();
        this.maxBranchLength = maxBranchLength;
        this.segmentCount = segmentCount;
        this.cursor = new int[]{0, 0};
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

        boolean[][] plan = clean(3, plan(rng.nextInt(size), rng.nextInt(size), size, segmentCount));

        int[] potentialEntrance = findRandom(plan, coords ->
                (get(coords[0], coords[1] + 1, plan) || get(coords[0] + 1, coords[1], plan) || get(coords[0] - 1, coords[1], plan))
                && adjacent(coords[0], coords[1], false, plan) == 1);

        int[] potentialExit = findRandom(plan, coords ->
                (get(coords[0], coords[1] + 1, plan) || get(coords[0] + 1, coords[1], plan) || get(coords[0] - 1, coords[1], plan))
                && adjacent(coords[0], coords[1], false, plan) == 1
                && coords[0] != potentialEntrance[0] && coords[1] != potentialEntrance[1]);

        plan[potentialEntrance[0]][potentialEntrance[1]] = true;
        plan[potentialExit[0]][potentialExit[1]] = true;

        for (int x = 0; x < plan.length; x++) {
            for (int y = 0; y < plan[0].length; y++) {
                if (plan[x][y]) map[x][y] = new DungeonRoomGenerator(get(x, y-1, plan), get(x, y+1, plan), get(x+1, y, plan), get(x-1, y, plan));
            }
        }

        map[potentialEntrance[0]][potentialEntrance[1]] = new DungeonEntranceGenerator(
                get(potentialEntrance[0], potentialEntrance[1]+1, plan),
                get(potentialEntrance[0]+1, potentialEntrance[1], plan),
                get(potentialEntrance[0]-1, potentialEntrance[1], plan));

        map[potentialExit[0]][potentialExit[1]] = new DungeonBossRoomGenerator();


    }

    private boolean[][] plan(int startX, int startY, int size, int segmentCount) {
        boolean[][] map = new boolean[size][size];
        cursor = new int[]{startX, startY};

        int forbiddenDirection = -1;

        for (int i = 0; i < segmentCount; i++) {
            int randomDirection = i == 0 ? 2 : (i == segmentCount - 1 ? 0 : rng.nextInt(4));
            if (randomDirection == forbiddenDirection) continue;
            if (randomDirection == 0 && cursor[1] == 0) continue;
            if (randomDirection == 1 && cursor[0] == size - 1) continue;
            if (randomDirection == 2 && cursor[1] == size - 1) continue;
            if (randomDirection == 3 && cursor[0] == 0) continue;
            segment(cursor, map, rng.nextInt(4));
            forbiddenDirection = (randomDirection + 2) % 4;
        }
        return map;
    }

    private void segment(int[] cursor, boolean[][] map, int direction) {

        int maxDist;
        int axis = direction % 2 == 0 ? 1 : 0; //rows and columns
        int delta = direction == 1 || direction == 2 ? 1 : -1; //up or down

        switch(direction) {
            case 0: maxDist = cursor[1]; break;
            case 1: maxDist = map.length - 1 - cursor[0]; break;
            case 2: maxDist = map.length - 1 - cursor[1]; break;
            case 3: maxDist = cursor[0]; break;
            default: maxDist = 0;
        }

        int dist = rng.nextInt((int)MiscMath.clamp(maxDist, 1, maxBranchLength));

        for (int i = 0; i < dist; i++) {
            map[cursor[0]][cursor[1]] = true;
            cursor[axis] += delta;
        }

    }

    private int adjacent(int x, int y, boolean includeCorners, boolean[][] map) {
        int count = 0;
        if (get(x, y-1, map)) count++;
        if (get(x, y+1, map)) count++;
        if (get(x-1, y, map)) count++;
        if (get(x+1, y, map)) count++;

        if (includeCorners) {
            if (get(x - 1, y - 1, map)) count++;
            if (get(x + 1, y - 1, map)) count++;
            if (get(x + 1, y + 1, map)) count++;
            if (get(x - 1, y + 1, map)) count++;
        }
        return count;
    }

    private boolean[][] clean(int passes, boolean[][] map) {
        if (passes == 0) return map;
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                int adjacent = adjacent(x, y, true, map);
                if (adjacent >= 6 || adjacent <= 1) map[x][y] = false;
            }
        }
        return clean(passes - 1, map);
    }

    private int[] findRandom(boolean[][] map, Predicate<int[]> isValid) {
        ArrayList<int[]> potentials = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                int[] potential = new int[]{x, y};
                boolean valid = isValid.test(potential);
                if (valid) potentials.add(potential);
            }
        }
        return potentials.get(rng.nextInt(potentials.size()));
    }

    private boolean get(int x, int y, boolean[][] map) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) return false;
        return map[x][y];
    }

    private void set(int x, int y, boolean value, boolean[][] map) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length) return;
        map[x][y] = value;
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