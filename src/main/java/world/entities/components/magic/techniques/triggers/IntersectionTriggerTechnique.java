package world.entities.components.magic.techniques.triggers;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class IntersectionTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {

    }

    @Override
    public void update(MagicSourceComponent cast) {
        cast.affectContinuous();
    }

}
