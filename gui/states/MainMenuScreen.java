package gui.states;

import assets.Assets;
import assets.definitions.Definitions;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
import gui.menus.Journal;
import gui.menus.SpellcraftingMenu;
import main.SlickInitializer;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import world.Camera;
import world.Chunk;
import world.Region;
import world.World;
import world.entities.Entity;
import world.entities.types.humanoids.npcs.LostCivilian;
import world.generators.region.DungeonGenerator;

import java.util.ArrayList;
import java.util.Random;

public class MainMenuScreen extends BasicGameState {

    static StateBasedGame game;
    private static Input input;
    private static boolean initialized;
    private Graphics graphics;

    private static GUI gui;
    private Image title_bg;

    public MainMenuScreen() {
        this.initialized = false;
    }

    @Override
    public int getID() {
        return Assets.MAIN_MENU_SCREEN;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        if (initialized) return;
        gui = new GUI();
        game = sbg;

        title_bg = Assets.getImage("assets/gui/title_bg.png", Image.FILTER_LINEAR);

        gui.addElement(new Button(null, 24, 24, "icons/play.png", true) {
            @Override
            public boolean onClick(int button) {
                World.init();
                GameScreen.initializeGUI();
                sbg.enterState(Assets.GAME_SCREEN);
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

        initialized = true;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        graphics = g;
        input = gc.getInput();
        g.setFont(Assets.getFont(14));
        g.drawImage(title_bg.getScaledCopy(Window.getWidth(), Window.getHeight()), 0, 0);
        gui.draw(g);

        g.setFont(Assets.getFont(14));


    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        MiscMath.DELTA_TIME = (int)(delta * World.getTimeMultiplier());
        input = gc.getInput();
    }

    @Override
    public void keyReleased(int key, char c) {
        if (key == Input.KEY_F11) {
            try {
                Window.toggleFullScreen();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }
        gui.onKeyUp(key);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        gui.handleMousePressed(x, y, button);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        gui.handleMouseRelease(x, y, button);
    }

    @Override
    public void keyPressed(int key, char c) {
        gui.onKeyDown(key);
        if (key == Input.KEY_F2)
            Window.takeScreenshot(graphics);
    }

    @Override
    public void mouseWheelMoved(int newValue) {
        gui.handleMouseScroll(newValue > 0 ? -1 : 1);
    }

    public static Input getInput() { return input; }

    public static GUI getGUI() {
        return gui;
    }

}
