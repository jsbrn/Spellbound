package world.generators.region;

import com.github.mathiewz.slick.Sound;
import world.generators.chunk.ChunkGenerator;

import java.util.Random;

public abstract class RegionGenerator {

    private Random rng;
    private int seed;

    public RegionGenerator(int seed)
    {
        this.rng = new Random(seed);
        this.seed = seed;
    }

    public final void setSeed(int seed) {
        this.rng.setSeed(seed);
    }

    public final int getSeed() { return seed; }

    public final Random rng() { return rng; }

    public abstract ChunkGenerator getChunkGenerator(int cx, int cy, int region_size);

    public abstract Sound getBackgroundAmbience();

}
