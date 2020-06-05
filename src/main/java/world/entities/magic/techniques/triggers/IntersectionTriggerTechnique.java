package world.entities.magic.techniques.triggers;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class IntersectionTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {
        cast.affectContinuous();
    }

}
