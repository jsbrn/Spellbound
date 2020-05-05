package gui.states;

import assets.Assets;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import main.GameManager;
import org.newdawn.slick.*;
import world.Chunk;
import world.World;

import java.io.File;

public class MainMenuScreen extends GameState {

    public MainMenuScreen() {
        super("assets/gui/title_bg.png");
    }

    @Override
    public int getID() {
        return GameState.MAIN_MENU;
    }

    @Override
    public void addGUIElements(GUI gui) {
        Button playButton = new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public boolean onClick(int button) {
                File f = new File(Assets.ROOT_DIRECTORY+"/world/world.json");
                if (!f.exists()) {
                    World.init();
                    World.generate(0);
                    World.spawnPlayer(Chunk.CHUNK_SIZE/2, Chunk.CHUNK_SIZE/2, 180, "player_home");
                    World.save();
                } else {
                    World.init();
                    World.load();
                }
                GameManager.getGameState(GameState.GAME_SCREEN).resetGUI();
                GameManager.switchTo(GameState.GAME_SCREEN);
                return true;
            }
        };
        gui.addElement(playButton, -(24*3)/2, 0, GUIAnchor.CENTER);
        Button settings = new Button(null, 24, 24, "icons/settings.png", true) {
            @Override
            public boolean onClick(int button) {
                return false;
            }
        };
        settings.setEnabled(false);
        gui.addElement(settings, 0, 0, GUIAnchor.CENTER);
        gui.addElement(new Button(null, 24, 24, "icons/quit.png", true) {
            @Override
            public boolean onClick(int button) {
                System.exit(0);
                return true;
            }
        }, (24*3)/2, 0, GUIAnchor.CENTER);

        gui.addElement(new IconLabel("title.png"), 0, 8, GUIAnchor.TOP_MIDDLE);
        gui.addElement(new TextLabel("Pre-Alpha", 8, Color.white, true, false), 32, 32, GUIAnchor.TOP_MIDDLE);
    }

    @Override
    public void onEnter() {

    }

}
