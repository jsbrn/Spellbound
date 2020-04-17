package gui.states;

import assets.Assets;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import main.Game;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import world.World;

public class MainMenuScreen extends GameState {

    private Image title_bg;

    public MainMenuScreen() {
        super("assets/gui/title_bg.png");
    }

    @Override
    public int getID() {
        return Game.MAIN_MENU;
    }

    @Override
    public void addGUIElements(GUI gui) {

        title_bg = Assets.getImage("assets/gui/title_bg.png", Image.FILTER_LINEAR);

        gui.addElement(new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public boolean onClick(int button) {
                World.init();
                Game.getGameState(Game.GAME_SCREEN).resetGUI();
                Game.switchTo(Game.GAME_SCREEN);
                return true;
            }
        }, -(24*3)/2, 0, GUIAnchor.CENTER);
        gui.addElement(new Button(null, 24, 24, "icons/settings.png", true) {
            @Override
            public boolean onClick(int button) {
                return false;
            }
        }, 0, 0, GUIAnchor.CENTER);
        gui.addElement(new Button(null, 24, 24, "icons/quit.png", true) {
            @Override
            public boolean onClick(int button) {
                System.exit(0);
                return true;
            }
        }, (24*3)/2, 0, GUIAnchor.CENTER);

        gui.addElement(new IconLabel("title.png"), 0, 8, GUIAnchor.TOP_MIDDLE);
        gui.addElement(new TextLabel("Pre-Alpha", 8, Color.white, true), 32, 32, GUIAnchor.TOP_MIDDLE);
    }

}
