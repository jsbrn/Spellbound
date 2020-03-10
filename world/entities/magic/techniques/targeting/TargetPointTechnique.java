package world.entities.magic.techniques.targeting;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class TargetPointTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.setTarget(
                cast.getCastCoordinates()[0],
                cast.getCastCoordinates()[1]);

    }

    @Override
    public void update(MagicSource cast) {

    }
}
