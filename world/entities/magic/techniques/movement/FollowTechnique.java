package world.entities.magic.techniques.movement;

import misc.Location;
import misc.MiscMath;
import world.entities.Entity;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

import java.util.ArrayList;

public class FollowTechnique extends Technique {

    private Location location;

    @Override
    public void applyTo(MagicSource source) {
        this.location = source.getBody().getLocation();
    }

    @Override
    public void update(MagicSource source) {
        if (source.getTarget() == null) {
            ArrayList<Entity> entities = location.getRegion().getEntities((int)location.getCoordinates()[0]-1, (int)location.getCoordinates()[1]-1, 3, 3);
            source.setTarget(entities.get((int)MiscMath.random(0, entities.size()-1)));
        } else {
            source.setMoveTarget(source.getTarget().getLocation().getCoordinates()[0], source.getTarget().getLocation().getCoordinates()[1] - 0.5f);
            if (location.distanceTo(source.getTarget().getLocation()) - 0.5 < 0.5)
                location.setCoordinates(source.getTarget().getLocation().getCoordinates()[0], source.getTarget().getLocation().getCoordinates()[1] - 0.5);
        }
    }

}
