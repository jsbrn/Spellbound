package world.magic.techniques.radius;

import misc.MiscMath;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class RadiusShrinkTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
    }

    @Override
    public void update(MagicSource source) {
        if (source.getBody().getDepthRadius() > 0)
            source.getBody().addDepthRadius(MiscMath.getConstant(-Math.max(0.5, source.getLevel("physical_speed")), 1));
    }

}
