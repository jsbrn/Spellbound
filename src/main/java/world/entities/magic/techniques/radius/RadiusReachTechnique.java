package world.entities.magic.techniques.radius;

import misc.MiscMath;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class RadiusReachTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
    }

    @Override
    public void update(MagicSource source) {
        source.getBody().addMinRadius(MiscMath.getConstant(getLevel(), 1));
    }

}
