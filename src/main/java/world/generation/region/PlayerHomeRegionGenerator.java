package world.generation.region;

import com.github.mathiewz.slick.Sound;
import gui.sound.SoundManager;
import org.json.simple.JSONObject;
import world.Tiles;

public class PlayerHomeRegionGenerator extends RegionGenerator {

    public PlayerHomeRegionGenerator(int seed) {
        super(seed);
    }

    @Override
    public byte getBase(int wx, int wy) {
        return Tiles.WOOD_FLOOR;
    }

    @Override
    public byte getTop(int wx, int wy) {
        return Tiles.AIR;
    }

    @Override
    public JSONObject getEntity(int wx, int wy) {
        return null;
    }

    @Override
    public Sound getBackgroundAmbience() {
        return SoundManager.WOOD_STOVE;
    }

}
