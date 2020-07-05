package world.entities.components.magic.techniques.rotation;

import misc.MiscMath;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class CounterSpinTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {

    }

    @Override
    public void update(MagicSourceComponent source) {
        source.addDirection(MiscMath.getConstant(-90 * source.getTorque(), 1));
    }

}
