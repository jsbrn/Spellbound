package gui.states;

import assets.Assets;
import gui.GUI;
import gui.GUIAnchor;
import gui.GUIElement;
import gui.elements.Backdrop;
import gui.elements.Hotbar;
import gui.elements.Statusbar;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import world.World;
import world.entities.actions.action.SetAnimationAction;

public class GameScreen extends BasicGameState {

    static StateBasedGame game;
    private Input input;
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
        game = sbg;
        gui = new GUI();
        gui.addChild(new Backdrop(), 0, 0, GUIAnchor.TOP_LEFT);
        GUIElement statusbar = new Statusbar(World.getPlayer()).addChild(new Hotbar(), 0, 32, GUIAnchor.TOP_LEFT);
        gui.addChild(statusbar, 1, 1, GUIAnchor.TOP_LEFT);
        Assets.loadTileSprite();
        World.init(16);

        init = true;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        float[] origin = MiscMath.getWorldOnscreenOrigin();
        gui.draw(g);
        World.draw(origin[0], origin[1], Window.getScale(), g);


    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        MiscMath.DELTA_TIME = delta;
        input = gc.getInput();
        World.update();

        int dx = 0, dy = 0;
        if (input.isKeyDown(Input.KEY_W)) {
            dy = -1;
        } else if (input.isKeyDown(Input.KEY_A)) {
            dx = -1;
        } else if (input.isKeyDown(Input.KEY_S)) {
            dy = 1;
        } else if (input.isKeyDown(Input.KEY_D)) {
            dx = 1;
        }

        if ((dx != 0 || dy != 0) && World.getPlayer().getActionQueue().isEmpty()) {
            World.getPlayer().queueAction(new SetAnimationAction("walking"));
            World.getPlayer().move(dx, dy);
        }

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

        if (key == Input.KEY_W || key == Input.KEY_A || key == Input.KEY_S || key == Input.KEY_D)
            World.getPlayer().queueAction(new SetAnimationAction("idle"));

    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        double[] mouse_wcoords = MiscMath.getWorldCoordinates(x, y);
        if (!gui.sendClick(x, y))
            World.getPlayer().getSpellbook().getSpell(0).cast(mouse_wcoords[0], mouse_wcoords[1], World.getPlayer());
    }

    @Override
    public void keyPressed(int key, char c) {

    }

}
