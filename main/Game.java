package main;

import assets.Assets;
import assets.definitions.Definitions;
import gui.states.GameScreen;
import gui.states.GameState;
import gui.states.MainMenuScreen;
import misc.*;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;

public class Game extends StateBasedGame {

    private static Game instance;
    private static String mouseCursor;

    public Game(String gameTitle) {

        super(gameTitle); //set window title to "gameTitle" string
        //add states
        addState(new GameScreen());
        addState(new MainMenuScreen());

        instance = this;

    }

    public static void main(String args[]) throws IOException {

        //initialize the window
        try {
            Window.WINDOW_INSTANCE = new AppGameContainer(new Game(Window.WINDOW_TITLE));
            Window.WINDOW_INSTANCE.setDisplayMode(16*65, 45*16, false);
            Window.WINDOW_INSTANCE.setTargetFrameRate(60);
            Window.WINDOW_INSTANCE.setAlwaysRender(true);
            Window.WINDOW_INSTANCE.setShowFPS(false);
            Window.setResizable(true);
            Window.WINDOW_INSTANCE.start();
        } catch (SlickException e) {

        }

    }

    public static void switchTo(int gameState) { instance.enterState(gameState); }
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

        //load assets and json files
        Assets.load();
        Definitions.load();

        //load "menu" state on startup
        this.enterState(GameState.MAIN_MENU);
    }

}