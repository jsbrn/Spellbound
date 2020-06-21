package world.magic.techniques.emission;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class DensityTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setDensity(getLevel());
    }

    @Override
    public void update(MagicSource source) {

    }

}
