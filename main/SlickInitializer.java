package main;

import assets.Assets;
import gui.states.GameScreen;
import misc.*;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import world.BinaryInsertTest;

import java.io.IOException;

public class SlickInitializer extends StateBasedGame {

    public SlickInitializer(String gameTitle) {

        super(gameTitle); //set window title to "gameTitle" string

        //add states
        addState(new GameScreen(Assets.GAME_SCREEN));
    }

    public static void main(String args[]) throws IOException {

        BinaryInsertTest binaryInsertTest = new BinaryInsertTest();
        binaryInsertTest.test();

        //initialize the window
        try {
            Window.WINDOW_INSTANCE = new AppGameContainer(new SlickInitializer(Window.WINDOW_TITLE));
            Window.WINDOW_INSTANCE.setDisplayMode(16*65, 45*16, false);
            Window.WINDOW_INSTANCE.setTargetFrameRate(59);
            Window.WINDOW_INSTANCE.setAlwaysRender(true);
            Window.WINDOW_INSTANCE.setShowFPS(false);
            Window.WINDOW_INSTANCE.start();
        } catch (SlickException e) {

        }

    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        //initialize states
        getState(Assets.GAME_SCREEN).init(gc, this);
        //load "menu" state on startup
        this.enterState(Assets.GAME_SCREEN);
    }

}