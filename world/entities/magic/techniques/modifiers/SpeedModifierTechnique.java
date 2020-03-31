package world.entities.magic.techniques.modifiers;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class SpeedModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.setMoveSpeed(getLevel() * 3);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
