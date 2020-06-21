package world.magic.techniques.physical;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class SpeedModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.setMoveSpeed(getLevel() * 3);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
