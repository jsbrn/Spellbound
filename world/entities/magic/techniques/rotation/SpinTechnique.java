package world.entities.magic.techniques.rotation;

import misc.MiscMath;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class SpinTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {

    }

    @Override
    public void update(MagicSource source) {
        source.setTargetDirection(source.getTargetDirection() + MiscMath.getConstant(source.getRotationSpeed(), 1));
    }

}
