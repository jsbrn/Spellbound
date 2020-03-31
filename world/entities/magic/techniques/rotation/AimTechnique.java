package world.entities.magic.techniques.rotation;

import gui.states.GameScreen;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Game;
import world.Camera;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class AimTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {

    }

    @Override
    public void update(MagicSource source) {
        double[] mwc = Camera.getWorldCoordinates(GameScreen.getInput().getMouseX(), GameScreen.getInput().getMouseY(), Window.getScale());
        double angle = MiscMath.angleBetween(source.getBody().getLocation().getCoordinates()[0], source.getBody().getLocation().getCoordinates()[1], mwc[0], mwc[1]);
        source.setDirection(angle);
    }

}
