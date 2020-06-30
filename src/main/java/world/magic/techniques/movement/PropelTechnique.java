package world.magic.techniques.movement;

import misc.Location;
import misc.MiscMath;
import world.entities.Entities;
import world.entities.components.LocationComponent;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class PropelTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {

        Integer caster = source.getCaster();
        double[] castTarget = source.getCastCoordinates();

        double[] moveTarget = MiscMath.getUnitVector(
                (float)castTarget[0] - (float)source.getBody().getLocation().getCoordinates()[0],
                (float)castTarget[1] - (float)source.getBody().getLocation().getCoordinates()[1]);
        Location casterLocation = ((LocationComponent) Entities.getComponent(LocationComponent.class, source.getCaster())).getLocation();
        moveTarget[0] = casterLocation.getCoordinates()[0] + (moveTarget[0] * Integer.MAX_VALUE); //far off into the distance
        moveTarget[1] = casterLocation.getCoordinates()[1] + (moveTarget[1] * Integer.MAX_VALUE);
        source.setMoveTarget(moveTarget[0], moveTarget[1]);

        double angle = MiscMath.angleBetween(source.getBody().getLocation().getCoordinates()[0], source.getBody().getLocation().getCoordinates()[1], moveTarget[0], moveTarget[1]);
        source.setDirection(angle);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
