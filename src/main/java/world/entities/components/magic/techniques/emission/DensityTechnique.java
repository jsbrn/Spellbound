package world.entities.components.magic.techniques.emission;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class DensityTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {
        source.getBody().setDensity(getLevel());
    }

    @Override
    public void update(MagicSourceComponent source) {

    }

}
