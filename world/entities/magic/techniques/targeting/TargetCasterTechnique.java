package world.entities.magic.techniques.targeting;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class TargetCasterTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {
        cast.setTarget(
                cast.getCaster().getLocation().getCoordinates()[0],
                cast.getCaster().getLocation().getCoordinates()[1] - 0.5);
    }
}
