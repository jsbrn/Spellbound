package world.entities.magic.techniques.movement;

import misc.MiscMath;
import world.entities.Entity;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class PropelTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {

        Entity caster = source.getCaster();
        double[] castTarget = source.getCastCoordinates();

        double[] moveTarget = MiscMath.getUnitVector(
                (float)castTarget[0] - (float)source.getBody().getCoordinates()[0],
                (float)castTarget[1] - (float)source.getBody().getCoordinates()[1]);
        moveTarget[0] = caster.getCoordinates()[0] + (moveTarget[0] * Integer.MAX_VALUE); //far off into the distance
        moveTarget[1] = caster.getCoordinates()[1] + (moveTarget[1] * Integer.MAX_VALUE);
        source.setMoveTarget(moveTarget[0], moveTarget[1]);

        double angle = MiscMath.angleBetween(source.getBody().getCoordinates()[0], source.getBody().getCoordinates()[1], moveTarget[0], moveTarget[1]);
        source.setDirection(angle);
        source.setTargetDirection(angle);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
