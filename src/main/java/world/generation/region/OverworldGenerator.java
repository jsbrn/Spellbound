package world.generation.region;

import com.github.mathiewz.slick.Sound;
import gui.sound.SoundManager;
import org.json.simple.JSONObject;
import world.Tiles;

public class OverworldGenerator extends RegionGenerator {

    public OverworldGenerator(int seed) {
        super(seed);
    }

    @Override
    public byte getBase(int wx, int wy) {
        return Tiles.GRASS;
    }

    @Override
    public byte getTop(int wx, int wy) {
        return Math.random() > 0.1 ? Tiles.AIR : Tiles.CHAIR;
    }

    @Override
    public JSONObject getEntity(int wx, int wy) {
        return null;
    }

    @Override
    public Sound getBackgroundAmbience() {
        return SoundManager.FOREST_AMBIENCE;
    }

}
