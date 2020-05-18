package gui.states;

import assets.Assets;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import gui.menus.PlayerCustomizationMenu;
import gui.menus.PopupMenu;
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

    public void startGame() {
        GameManager.getGameState(GameState.GAME_SCREEN).resetGUI();
        World.setPaused(true);
        GameManager.getGameState(GameState.GAME_SCREEN).getGUI().stackModal(new PopupMenu(
                "Please Note",
                "",
                "This is a very simple demo of the core mechanics (the spell crafting and dungeon crawling). " +
                        "It is NOT the final game. There will be bugs. " +
                        "I'll be releasing the Alpha version of the game soon. " +
                        "Follow the itch.io devlog to stay up-to-date.",
                "icons/tome.png",
                Color.white));
        GameManager.switchTo(GameState.GAME_SCREEN);
    }

    @Override
    public void addGUIElements(GUI gui) {
        Button playButton = new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public void onClick(int button) {
                File f = new File(Assets.ROOT_DIRECTORY+"/world/world.json");
                if (!f.exists()) {
                    gui.stackModal(new PlayerCustomizationMenu());
                } else {
                    World.init(null);
                    World.load();
                    startGame();
                }
            }
        };
        gui.addElement(playButton, -(24*3)/2, 0, GUIAnchor.CENTER);
        Button settings = new Button(null, 24, 24, "icons/settings.png", true) {
            @Override
            public void onClick(int button) {
            }
        };
        settings.setEnabled(false);
        gui.addElement(settings, 0, 0, GUIAnchor.CENTER);
        gui.addElement(new Button(null, 24, 24, "icons/quit.png", true) {
            @Override
            public void onClick(int button) {
                System.exit(0);
            }
        }, (24*3)/2, 0, GUIAnchor.CENTER);

        gui.addElement(new IconLabel("title.png"), 0, 8, GUIAnchor.TOP_MIDDLE);
        gui.addElement(new TextLabel("Demo", 8, Color.white, true, false), 0, 32, GUIAnchor.TOP_MIDDLE);
    }

    @Override
    public void onEnter() {

    }

}
