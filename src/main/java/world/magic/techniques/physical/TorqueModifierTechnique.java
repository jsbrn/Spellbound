package world.magic.techniques.physical;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class TorqueModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.setTorque(getLevel());
    }

    @Override
    public void update(MagicSource cast) {

    }

}
