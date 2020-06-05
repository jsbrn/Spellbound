package world.entities.actions.types;

import gui.states.GameState;
import main.GameManager;
import org.newdawn.slick.Color;
import world.World;
import world.entities.actions.Action;

public class SpeakAction extends Action {

    private String text;

    public SpeakAction(String text) {
        this.text = text;
    }

    @Override
    public void onStart() {
        if (getParent().getLocation().getRegion().equals(World.getLocalPlayer().getLocation().getRegion()))
            GameManager.getGameState(GameState.GAME_SCREEN).getGUI().floatText(getParent().getLocation(), text, Color.white, 1, Math.max(1000, 400 * text.split("\\s").length), -1, false);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return true;
    }

    @Override
    public void onFinish() {
        
    }

    public String toString() { return "Speak("+text+")"; }

}
