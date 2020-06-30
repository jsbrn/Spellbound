package world.magic.techniques.movement;

import misc.Location;
import world.entities.Entities;
import world.entities.components.LocationComponent;
import world.magic.MagicSource;

public class AuraTechnique extends PropelTechnique {

    @Override
    public void applyTo(MagicSource source) {
        super.applyTo(source);
    }

    @Override
    public void update(MagicSource source) {
        Location sourceLocation = ((LocationComponent) Entities.getComponent(LocationComponent.class, source.getCaster())).getLocation();
        source.setMoveTarget(
                sourceLocation.getCoordinates()[0],
                sourceLocation.getCoordinates()[1]);
    }

}
