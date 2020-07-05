package world.entities.components.magic.techniques.rotation;

import gui.states.GameState;
import main.GameManager;
import misc.MiscMath;
import misc.Window;
import world.Camera;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class AimTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {

    }

    @Override
    public void update(MagicSourceComponent source) {
        double[] mwc = Camera.getWorldCoordinates(GameManager.getGameState(GameState.GAME_SCREEN).getInput().getMouseX(),
                GameManager.getGameState(GameState.GAME_SCREEN).getInput().getMouseY(), Window.getScale());
        double angle = MiscMath.angleBetween(source.getBody().getLocation().getCoordinates()[0], source.getBody().getLocation().getCoordinates()[1], mwc[0], mwc[1]);
        source.setDirection(angle);
    }

}
