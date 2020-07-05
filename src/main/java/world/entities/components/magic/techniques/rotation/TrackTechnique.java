package world.entities.components.magic.techniques.rotation;

import misc.MiscMath;
import world.entities.Entities;
import world.entities.components.LocationComponent;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class TrackTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {

    }

    @Override
    public void update(MagicSourceComponent source) {
        if (source.getTarget() == null) return;
        double[] target = ((LocationComponent) Entities.getComponent(LocationComponent.class, source.getTarget())).getLocation().getCoordinates();
        double angle = MiscMath.angleBetween(source.getBody().getLocation().getCoordinates()[0], source.getBody().getLocation().getCoordinates()[1], target[0], target[1]);
        source.setDirection(angle);
    }

}
