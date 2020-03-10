package world.entities.magic.techniques.radius;

import misc.MiscMath;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class RadiusExpandTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
    }

    @Override
    public void update(MagicSource source) {
        source.getBody().addMaxRadius(MiscMath.getConstant(getLevel(), 0.5));
    }

}
