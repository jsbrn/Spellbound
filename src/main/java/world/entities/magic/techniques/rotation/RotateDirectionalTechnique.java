package world.entities.magic.techniques.rotation;

import gui.states.GameScreen;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import world.Camera;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

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
