package world.entities.components.magic.techniques.physical;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class TorqueModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {
        cast.setTorque(getLevel());
    }

    @Override
    public void update(MagicSourceComponent cast) {

    }

}
