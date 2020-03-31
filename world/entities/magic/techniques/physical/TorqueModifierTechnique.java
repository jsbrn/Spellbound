package world.entities.magic.techniques.physical;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class TorqueModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.setTorque(getLevel());
    }

    @Override
    public void update(MagicSource cast) {

    }

}
