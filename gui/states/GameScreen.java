package gui.states;

import assets.Assets;
import assets.definitions.Definitions;
import gui.GUI;
import gui.GUIAnchor;
import gui.GUIElement;
import gui.elements.Button;
import gui.elements.Hotbar;
import gui.elements.Label;
import gui.elements.Statusbar;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import world.World;

public class GameScreen extends BasicGameState {

    static StateBasedGame game;
    private static Input input;
    private boolean init;

    private GUI gui;

    public GameScreen(int state) {
        this.init = false;
    }

    @Override
    public int getID() {
        return Assets.GAME_SCREEN;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        if (init) return;
        World.init(16);
        game = sbg;
        gui = new GUI();
        gui.addChild(new Statusbar(World.getPlayer()), 2, 2, GUIAnchor.TOP_LEFT);
        gui.addChild(new Hotbar(World.getPlayer()), 2, 38, GUIAnchor.TOP_LEFT);
        gui.addChild(new Button("spellbook.png") {
            @Override
            public boolean onMouseRelease(int ogx, int ogy) {
                return false;
            }
        }, 4, 94, GUIAnchor.TOP_LEFT);
        Assets.load();
        Definitions.load();

        init = true;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        float[] origin = MiscMath.getWorldOnscreenOrigin();
        World.draw(origin[0], origin[1], Window.getScale(), g);
        gui.draw(g);

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        MiscMath.DELTA_TIME = delta;
        input = gc.getInput();
        World.update();

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
        super.mousePressed(button, x, y);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        double[] mouse_wcoords = MiscMath.getWorldCoordinates(x, y);
        gui.onMouseRelease(x, y, button);
    }

    @Override
    public void keyPressed(int key, char c) {
        gui.onKeyDown(key);
    }

    public static Input getInput() { return input; }

}
