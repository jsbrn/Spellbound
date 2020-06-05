package main;

import assets.Assets;
import assets.Settings;
import gui.sound.SoundManager;
import gui.states.*;
import misc.*;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.io.File;
import java.io.IOException;

public class GameManager extends StateBasedGame {

    private static GameManager instance;
    private static String mouseCursor;

    private static boolean loadMusic;

    public GameManager(String gameTitle) {

        super(gameTitle); //set window title to "gameTitle" string
        //add states
        addState(new GameScreen());
        addState(new MainMenuScreen());
        addState(new LoadingScreen(loadMusic));
        addState(new SettingsScreen());

        instance = this;

    }

    public static void main(String args[]) throws IOException {

        loadMusic = args.length <= 0 || Boolean.parseBoolean(args[0]);

        new File(Assets.ROOT_DIRECTORY+ "/world").mkdirs();
        new File(Assets.ASSETS_DIRECTORY+"/sounds/").mkdirs();

        Settings.load();

        //initialize the window
        try {
            Window.WINDOW_INSTANCE = new AppGameContainer(new GameManager(Window.WINDOW_TITLE));
            DisplayMode desktop = Window.getAllDisplayModes().get(0);
            Window.WINDOW_INSTANCE.setDisplayMode((int)(Window.getScreenWidth() * 0.75), (int)(Window.getScreenHeight() * 0.75), false);
            Window.WINDOW_INSTANCE.setSmoothDeltas(false);
            Window.WINDOW_INSTANCE.setAlwaysRender(true);
            Window.WINDOW_INSTANCE.setVSync(true);
            Window.WINDOW_INSTANCE.setShowFPS(false);
            Window.WINDOW_INSTANCE.setUpdateOnlyWhenVisible(false);
            Window.WINDOW_INSTANCE.setIcons(new String[]{
                    "gui/icons/favicon/icon_16x16.png",
                    "gui/icons/favicon/icon_32x32.png",
                    "gui/icons/favicon/icon_48x48.png",
                    "gui/icons/favicon/icon_64x64.png",
                    "gui/icons/favicon/icon_80x80.png",
                    "gui/icons/favicon/icon_96x96.png",
                    "gui/icons/favicon/icon_112x112.png",
                    "gui/icons/favicon/icon_128x128.png"
            });
            if (Settings.getBoolean("fullscreen")) Window.toggleFullScreen();
            Window.WINDOW_INSTANCE.start();

        } catch (SlickException e) {

        }

        SoundManager.cleanup();

    }

    public static void switchTo(int gameState, boolean transition) {
        instance.enterState(gameState, transition ? new FadeOutTransition() : null, transition ? new FadeInTransition() : null);
        ((GameState)instance.getState(gameState)).onEnter();
    }

    public static void setMouseCursor(String ref) {
        try {
            mouseCursor = ref;
            instance.getContainer().setMouseCursor(
                    Assets.getImage(mouseCursor)
                            .getFlippedCopy(false, false)
                            .getScaledCopy(4), 0, 0);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    public static GameState getGameState(int id) { return (GameState)instance.getState(id); }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        //initialize states
        getState(GameState.GAME_SCREEN).init(gc, this);
        getState(GameState.MAIN_MENU).init(gc, this);
        getState(GameState.SETTINGS_SCREEN).init(gc, this);

        //load "menu" state on startup
        this.enterState(GameState.LOADING_SCREEN);
    }

}