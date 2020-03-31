package world.entities.magic.techniques.movement;

import misc.Location;
import world.entities.magic.MagicSource;

public class FollowTechnique extends PropelTechnique {

    private Location targetLocation;

    @Override
    public void applyTo(MagicSource source) {
        super.applyTo(source);
        this.targetLocation = new Location(source.getBody().getLocation());
    }

    @Override
    public void update(MagicSource source) {
        if (source.getTarget() == null) { super.update(source); return; }
        source.setMoveTarget(source.getTarget().getLocation().getCoordinates()[0], source.getTarget().getLocation().getCoordinates()[1] - 0.5f);
        targetLocation.setCoordinates(source.getTarget().getLocation().getCoordinates()[0], source.getTarget().getLocation().getCoordinates()[1] - 0.5);
        if (targetLocation.distanceTo(source.getBody().getLocation()) < 0.1)
            source.getBody().getLocation().setCoordinates(
                    targetLocation.getCoordinates()[0],
                    targetLocation.getCoordinates()[1]);
    }

}
