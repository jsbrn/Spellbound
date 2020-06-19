package gui.states;

import assets.Assets;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import gui.elements.Button;
import gui.menus.PlayerCustomizationMenu;
import gui.menus.PopupMenu;
import gui.sound.SoundManager;
import main.GameManager;
import misc.Window;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import com.github.mathiewz.slick.Color;
import world.World;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainMenuScreen extends GameState {

    public MainMenuScreen() {
        super("gui/title_bg.png");
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
        GameManager.switchTo(GameState.GAME_SCREEN, true);
        SoundManager.registerEvents();
    }

    @Override
    public void addGUIElements(GUI gui) {

        String[] tips = Assets.read("tips.txt", true).split("\\n");
        gui.addElement(new TextLabel("Tip of the Day", 6, Color.white, true, false), 0, 32, GUIAnchor.CENTER);
        gui.addElement(new TextLabel(tips[new Random().nextInt(tips.length)], 4, 32*5, 8, Color.white, true, false), 0, 48, GUIAnchor.CENTER);

        Button deleteSave = new Button("Open root directory", 52, 8, null, true) {
            @Override
            public void onClick(int button) {
                Window.setFullscreen(false);
                try {
                    Desktop.getDesktop().open(new File(Assets.ROOT_DIRECTORY));
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
                    //make a backup before loading a save
                    try {
                        new File(Assets.ROOT_DIRECTORY+"/backups/").mkdirs();
                        new ZipFile(Assets.ROOT_DIRECTORY+"/backups/backup_"+System.currentTimeMillis()+".zip")
                                .addFolder(new File(Assets.ROOT_DIRECTORY+"/world/"), new ZipParameters());
                    } catch (ZipException e) {
                        e.printStackTrace();
                    }
                    World.load();
                    startGame();
                }
                deleteSave.setEnabled(true);
            }
        };

        playButton.setTooltipText("Play the game!");
        gui.addElement(playButton, -(24*3)/2, 0, GUIAnchor.CENTER);

        Button quit = new Button(null, 24, 24, "icons/quit.png", true) {
            @Override
            public void onClick(int button) {
                System.exit(0);
            }
        };
        quit.setTooltipText("Quit");
        gui.addElement(quit, (24*3)/2, 0, GUIAnchor.CENTER);

        Button settings = new Button(null, 24, 24, "icons/settings.png", true) {
            @Override
            public void onClick(int button) {
                GameManager.switchTo(GameState.SETTINGS_SCREEN, false);
            }
        };
        settings.setTooltipText("Change settings");
        gui.addElement(settings, 0, 0, GUIAnchor.CENTER);

        gui.addElement(new IconLabel("title.png"), 0, 8, GUIAnchor.TOP_MIDDLE);
        gui.addElement(new TextLabel("Demo", 8, Color.white, true, false), 0, 32, GUIAnchor.TOP_MIDDLE);
        gui.addElement(deleteSave, -2, -2, GUIAnchor.BOTTOM_RIGHT);

        gui.addElement(new Button("Visit the website", 48, 8, null, true) {
            @Override
            public void onClick(int button) {
                Window.setFullscreen(false);
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
