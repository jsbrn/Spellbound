package world.generators.region;

import gui.sound.SoundManager;
import misc.MiscMath;
import com.github.mathiewz.slick.Sound;
import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.overworld.*;

import java.util.Random;

public class DefaultWorldGenerator extends RegionGenerator {

    private int[] well;

    public DefaultWorldGenerator(int seed) {
        super(seed);
    }

    public ChunkGenerator getChunkGenerator(int x, int y, int size) {
        if (well == null) well = new int[]{rng().nextInt(size), rng().nextInt(size)};
        int chunk_seed = getSeed() + x + y;
        if (x == well[0] && y == well[1]) return new WishingWellFieldGenerator(chunk_seed);
        if (x == size/2 && y == size/2) return new BackyardGenerator(chunk_seed);
        if (x == size/2 && y == (size/2) + 1) return new TrapdoorFieldGenerator(1, chunk_seed);
        if (rng().nextFloat() <= 0.07)
            return rng().nextBoolean() ? new BanditCampGenerator(chunk_seed) : new GraveyardGenerator(chunk_seed);
        return rng().nextFloat() <= 0.04
                ? new TrapdoorFieldGenerator((int)(MiscMath.distance(x, y, size/2, size/2)) / 2, chunk_seed)
                : (rng().nextFloat() < 0.25f ? new OpenFieldGenerator(chunk_seed) : new ForestGenerator(chunk_seed));
    }

    @Override
    public Sound getBackgroundAmbience() {
        return SoundManager.FOREST_AMBIENCE;
    }

}
