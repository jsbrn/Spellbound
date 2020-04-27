package gui.states;

import assets.Assets;
import gui.GUI;
import main.GameManager;
import misc.Window;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameState extends BasicGameState {

    public static final int GAME_SCREEN = 0, MAIN_MENU = 1;

    private String background;
    private Input input;
    private GUI gui;

    public GameState() {
        gui = new GUI(this);
    }
    public GameState(String background) {
        this();
        this.background = background;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        gc.setDefaultFont(Assets.getFont(14));
        input = gc.getInput();
        addGUIElements(gui);
        GameManager.setMouseCursor("assets/gui/cursors/default.png");
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if (background != null) g.drawImage(Assets.getImage(background, Image.FILTER_LINEAR).getScaledCopy(Window.getWidth(), Window.getHeight()), 0, 0);
        getGUI().draw(g);
        if (Window.wasResized()) onResize(Window.getWidth(), Window.getHeight());
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

    }

    public final GUI getGUI() { return gui; }
    public final void resetGUI() {
        gui.reset();
        addGUIElements(gui);
    }
    protected abstract void addGUIElements(GUI gui);
    public final Input getInput() { return input; }

    @Override
    public void keyReleased(int key, char c) {

        if (key == getInput().KEY_F2) Window.takeScreenshot();

        if (key == getInput().KEY_F11) {
            try {
                Window.toggleFullScreen();
            } catch (SlickException e) {
                e.printStackTrace();
            }
        }

        getGUI().onKeyUp(key);

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
    }

    @Override
    public void mouseWheelMoved(int newValue) {
        gui.handleMouseScroll(newValue > 0 ? -1 : 1);
    }

    public abstract void onResize(int width, int height);

}
