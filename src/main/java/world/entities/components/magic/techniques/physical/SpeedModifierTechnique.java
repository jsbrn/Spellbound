package world.entities.components.magic.techniques.physical;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class SpeedModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {
        cast.setMoveSpeed(getLevel() * 3);
    }

    @Override
    public void update(MagicSourceComponent cast) {

    }

}
