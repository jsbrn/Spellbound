package world.entities.components.magic.techniques.radius;

import misc.MiscMath;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class RadiusShrinkTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {
    }

    @Override
    public void update(MagicSourceComponent source) {
        if (source.getBody().getDepthRadius() > 0)
            source.getBody().addDepthRadius(MiscMath.getConstant(-Math.max(0.5, source.getLevel("physical_speed")), 1));
    }

}
