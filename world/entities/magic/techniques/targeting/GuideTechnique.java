package world.entities.magic.techniques.targeting;

import gui.states.GameScreen;
import misc.MiscMath;
import misc.Window;
import world.Camera;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class GuideTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {
        int mx = GameScreen.getInput().getMouseX();
        int my = GameScreen.getInput().getMouseY();
        double[] world_coords = Camera.getWorldCoordinates(mx, my, Window.getScale());
        cast.setTarget(world_coords[0], world_coords[1]);
    }

}
