package world.magic.techniques.movement;

import misc.Location;
import world.magic.MagicSource;

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
    }

}
