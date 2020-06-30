package world.generators.region;

import com.github.mathiewz.slick.Sound;
import gui.sound.SoundManager;
import world.generators.chunk.ChunkGenerator;
import world.generators.chunk.interiors.PlayerHomeGenerator;

public class PlayerHomeRegionGenerator extends RegionGenerator {

    public PlayerHomeRegionGenerator(int seed) {
        super(seed);
    }

    @Override
    public ChunkGenerator getChunkGenerator(int cx, int cy, int size) {
        return new PlayerHomeGenerator(getSeed());
    }

    @Override
    public Sound getBackgroundAmbience() {
        return SoundManager.WOOD_STOVE;
    }

}
