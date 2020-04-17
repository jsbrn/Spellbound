package gui.menus;

import assets.Assets;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.Modal;
import gui.elements.TextLabel;
import gui.states.GameState;
import main.Game;
import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import world.World;

public class PauseMenu extends Modal {

    public PauseMenu() {
        super(Assets.getImage("assets/gui/blank.png"));
        addChild(new TextLabel("Paused", 5, Color.white, true), 0, -48, GUIAnchor.TOP_MIDDLE);
        addChild(new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public boolean onClick(int button) {
                getGUI().popModal();
                return true;
            }
        }, -16, 0, GUIAnchor.CENTER);

        addChild(new Button(null, 24, 24, "icons/save.png", true) {
            @Override
            public boolean onClick(int button) {
                Game.switchTo(GameState.MAIN_MENU);
                return true;
            }
        }, 16, 0, GUIAnchor.CENTER);
    }

    @Override
    public boolean onKeyUp(int key) {
        if (key == Input.KEY_ESCAPE) {
            getGUI().popModal();
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int key) {
        return true;
    }

    @Override
    public void onShow() {
        World.setPaused(true);
    }

    @Override
    public void onHide() {
        World.setPaused(false);
    }
}
