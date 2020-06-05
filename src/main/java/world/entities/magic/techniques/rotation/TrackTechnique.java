package world.entities.magic.techniques.rotation;

import gui.states.GameScreen;
import misc.MiscMath;
import misc.Window;
import world.Camera;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class TrackTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {

    }

    @Override
    public void update(MagicSource source) {
        if (source.getTarget() == null) return;
        double[] target = source.getTarget().getLocation().getCoordinates();
        double angle = MiscMath.angleBetween(source.getBody().getLocation().getCoordinates()[0], source.getBody().getLocation().getCoordinates()[1], target[0], target[1]);
        source.setDirection(angle);
    }

}
