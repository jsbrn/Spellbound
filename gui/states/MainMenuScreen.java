package gui.states;

import assets.Assets;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import gui.menus.PlayerCustomizationMenu;
import gui.menus.PopupMenu;
import gui.sound.SoundManager;
import main.GameManager;
import org.newdawn.slick.*;
import world.Chunk;
import world.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        SoundManager.registerEvents();
    }

    @Override
    public void addGUIElements(GUI gui) {

        Button deleteSave = new Button("Delete your save file", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                try {
                    Files.walk(Paths.get(Assets.ROOT_DIRECTORY+"/world/"))
                            .map(Path::toFile)
                            .sorted((o1, o2) -> -o1.compareTo(o2))
                            .forEach(File::delete);
                    setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };

        Button playButton = new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public void onClick(int button) {
                File f = new File(Assets.ROOT_DIRECTORY+"/world/world.json");
                if (!f.exists()) {
                    World.init(null);
                    gui.stackModal(new PlayerCustomizationMenu());
                } else {
                    World.init(null);
                    World.load();
                    startGame();
                }
                deleteSave.setEnabled(true);
            }
        };
        playButton.setTooltipText("Play the game!");
        gui.addElement(playButton, -(16*3)/2, 0, GUIAnchor.CENTER);
        Button quit = new Button(null, 24, 24, "icons/quit.png", true) {
            @Override
            public void onClick(int button) {
                System.exit(0);
            }
        };
        quit.setTooltipText("Quit");
        gui.addElement(quit, (16*3)/2, 0, GUIAnchor.CENTER);

        gui.addElement(new IconLabel("title.png"), 0, 8, GUIAnchor.TOP_MIDDLE);
        gui.addElement(new TextLabel("Demo", 8, Color.white, true, false), 0, 32, GUIAnchor.TOP_MIDDLE);
        gui.addElement(deleteSave, -2, -2, GUIAnchor.BOTTOM_RIGHT);

        gui.addElement(new Button("Visit the website", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                String url_open ="https://computerology.itch.io/spellbound";
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 2, -2, GUIAnchor.BOTTOM_LEFT);

    }

    @Override
    public void onEnter() {

    }

}
