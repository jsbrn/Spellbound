package world.generation.region;

import com.github.mathiewz.slick.Sound;
import org.json.simple.JSONObject;

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

    public abstract byte getBase(int wx, int wy);

    public abstract byte getTop(int wx, int wy);

    public abstract JSONObject getEntity(int wx, int wy);

    public abstract Sound getBackgroundAmbience();

}
