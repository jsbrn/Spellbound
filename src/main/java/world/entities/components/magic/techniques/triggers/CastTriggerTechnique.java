package world.entities.components.magic.techniques.triggers;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class CastTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {
        cast.affectOnce();
    }

    @Override
    public void update(MagicSourceComponent cast) {

    }

}
