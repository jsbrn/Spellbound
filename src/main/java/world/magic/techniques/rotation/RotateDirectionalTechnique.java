package world.magic.techniques.rotation;

import misc.Location;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class RotateDirectionalTechnique extends Technique {

    private Location last;

    @Override
    public void applyTo(MagicSource source) {
        this.last = new Location(source.getBody().getLocation());
    }

    @Override
    public void update(MagicSource source) {
        double angle = last.angleBetween(source.getBody().getLocation());
        source.setDirection(angle);
        last = new Location(source.getBody().getLocation());
    }

}
