package gui.states;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Input;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.IconLabel;
import gui.elements.TextLabel;
import gui.menus.ServerSelectMenu;
import main.GameManager;
import misc.Window;
import network.MPServer;

import java.awt.Desktop;
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

    @Override
    public void addGUIElements(GUI gui) {

        String[] tips = Assets.read("tips.txt", true).split("\\n");
        gui.addElement(new TextLabel("Tip of the Day", 6, Color.white, true, false), 0, 32, GUIAnchor.CENTER);
        gui.addElement(new TextLabel(tips[new Random().nextInt(tips.length)], 4, 32*5, 8, Color.white, Color.white, true, false), 0, 48, GUIAnchor.CENTER);

        TextLabel openRoot = new TextLabel("Open root directory", 3, Color.white, Color.yellow, true, false) {
            @Override
            public boolean onClick(int button) {
                Window.setFullscreen(false);
                try {
                    Desktop.getDesktop().open(new File(Assets.ROOT_DIRECTORY));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

        };

        Button playButton = new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public void onClick(int button) {
                gui.stackModal(new ServerSelectMenu());
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
        gui.addElement(new TextLabel("Alpha Candidate", 8, Color.white, true, false), 0, 32, GUIAnchor.TOP_MIDDLE);
        gui.addElement(openRoot, -2, 2, GUIAnchor.TOP_RIGHT);

        gui.addElement(new TextLabel("Visit the website", 3, Color.white, Color.yellow, true, false) {
            @Override
            public boolean onClick(int button) {
                Window.setFullscreen(false);
                String url_open ="https://computerology.itch.io/spellbound";
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(url_open));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }, -2, 6, GUIAnchor.TOP_RIGHT);

    }

    @Override
    public void onEnter() {

    }

}
