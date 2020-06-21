package world.magic.techniques.triggers;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class IntersectionTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {
        cast.affectContinuous();
    }

}
