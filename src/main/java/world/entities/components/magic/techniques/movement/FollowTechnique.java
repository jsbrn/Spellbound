package world.entities.components.magic.techniques.movement;

import misc.Location;
import world.entities.Entities;
import world.entities.components.LocationComponent;
import world.entities.components.magic.MagicSourceComponent;

public class FollowTechnique extends PropelTechnique {

    private Location targetLocation;

    @Override
    public void applyTo(MagicSourceComponent source) {
        super.applyTo(source);
        this.targetLocation = new Location(source.getBody().getLocation());
    }

    @Override
    public void update(MagicSourceComponent source) {
        if (source.getTarget() == null) { super.update(source); return; }
        Location targetLocation = ((LocationComponent) Entities.getComponent(LocationComponent.class, source.getTarget())).getLocation();
        source.setMoveTarget(targetLocation.getCoordinates()[0], targetLocation.getCoordinates()[1] - 0.5f);
    }

}
