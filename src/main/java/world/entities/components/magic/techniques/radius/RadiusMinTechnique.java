package world.entities.components.magic.techniques.radius;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class RadiusMinTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {
        source.getBody().setReachRadius(getLevel() / 2f);
    }

    @Override
    public void update(MagicSourceComponent source) {

    }

}
