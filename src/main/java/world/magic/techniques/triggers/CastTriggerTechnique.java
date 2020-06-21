package world.magic.techniques.triggers;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class CastTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.affectOnce();
    }

    @Override
    public void update(MagicSource cast) {

    }

}
