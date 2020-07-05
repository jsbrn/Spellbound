package world.entities.components.magic.techniques.movement;

import misc.Location;
import network.MPServer;
import world.entities.components.LocationComponent;
import world.entities.components.magic.MagicSourceComponent;

public class AuraTechnique extends PropelTechnique {

    @Override
    public void applyTo(MagicSourceComponent source) {
        super.applyTo(source);
    }

    @Override
    public void update(MagicSourceComponent source) {
        Location sourceLocation = ((LocationComponent) MPServer.getWorld().getEntities().getComponent(LocationComponent.class, source.getCaster())).getLocation();
        source.setMoveTarget(
                sourceLocation.getCoordinates()[0],
                sourceLocation.getCoordinates()[1]);
    }

}
