package world.entities.components.magic.techniques.rotation;

import misc.Location;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class RotateDirectionalTechnique extends Technique {

    private Location last;

    @Override
    public void applyTo(MagicSourceComponent source) {
        this.last = new Location(source.getBody().getLocation());
    }

    @Override
    public void update(MagicSourceComponent source) {
        double angle = last.angleBetween(source.getBody().getLocation());
        source.setDirection(angle);
        last = new Location(source.getBody().getLocation());
    }

}
