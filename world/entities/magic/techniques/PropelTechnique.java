package world.entities.magic.techniques;

import misc.MiscMath;
import world.entities.Entity;
import world.entities.magic.MagicSource;

public class PropelTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        Entity caster = cast.getCaster();
        double[] castCoordinates = cast.getCastCoordinates();

        double[] moveTarget = MiscMath.getUnitVector(
                (float)castCoordinates[0] - (float)caster.getCoordinates()[0],
                (float)castCoordinates[1] - (float)caster.getCoordinates()[1]);
        moveTarget[0] = caster.getCoordinates()[0] + (moveTarget[0] * 1000); //far off into the distance
        moveTarget[1] = caster.getCoordinates()[1] + (moveTarget[1] * 1000);
        cast.setMoveTarget(moveTarget[0], moveTarget[1]);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
