package world.entities.components.magic.techniques.radius;

import misc.MiscMath;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class RadiusReachTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {
    }

    @Override
    public void update(MagicSourceComponent source) {
        source.getBody().addMinRadius(MiscMath.getConstant(getLevel(), 1));
    }

}
