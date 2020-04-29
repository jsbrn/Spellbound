package main;

import assets.Assets;
import assets.definitions.Definitions;
import gui.states.GameScreen;
import gui.states.GameState;
import gui.states.MainMenuScreen;
import misc.*;
import org.lwjgl.opengl.Display;
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

    public GameManager(String gameTitle) {

        super(gameTitle); //set window title to "gameTitle" string
        //add states
        addState(new GameScreen());
        addState(new MainMenuScreen());

        instance = this;

    }

    public static void main(String args[]) throws IOException {

        File root = new File(Assets.ROOT_DIRECTORY+"/world");
        if (!root.exists()) root.mkdirs();

        //initialize the window
        try {
            Window.WINDOW_INSTANCE = new AppGameContainer(new GameManager(Window.WINDOW_TITLE));
            Window.WINDOW_INSTANCE.setDisplayMode(800, 600, false);
            Window.WINDOW_INSTANCE.setTargetFrameRate(60);
            Window.WINDOW_INSTANCE.setAlwaysRender(true);
            Window.WINDOW_INSTANCE.setShowFPS(false);
            Window.WINDOW_INSTANCE.setUpdateOnlyWhenVisible(false);
            Window.WINDOW_INSTANCE.setAlwaysRender(true);
            Window.WINDOW_INSTANCE.setIcons(new String[]{
                    "assets/gui/icons/favicon/icon_16x16.png",
                    "assets/gui/icons/favicon/icon_32x32.png",
                    "assets/gui/icons/favicon/icon_48x48.png",
                    "assets/gui/icons/favicon/icon_64x64.png",
                    "assets/gui/icons/favicon/icon_80x80.png",
                    "assets/gui/icons/favicon/icon_96x96.png",
                    "assets/gui/icons/favicon/icon_112x112.png",
                    "assets/gui/icons/favicon/icon_128x128.png"
            });
            //Window.toggleFullScreen();
            Window.WINDOW_INSTANCE.start();
        } catch (SlickException e) {

        }

    }

    public static void switchTo(int gameState) {
        instance.enterState(gameState, new FadeOutTransition(), new FadeInTransition());
        ((GameState)instance.getState(gameState)).onEnter();
    }
    public static void setMouseCursor(String ref) {
        try {
            mouseCursor = ref;
            instance.getContainer().setMouseCursor(
                    Assets.getImage(mouseCursor)
                            .getFlippedCopy(false, false)
                            .getScaledCopy(Window.getScale()), 0, 0);
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

        Assets.load();
        Definitions.load();

        //load "menu" state on startup
        this.enterState(GameState.MAIN_MENU);
    }

}