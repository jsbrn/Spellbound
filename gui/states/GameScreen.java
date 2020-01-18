package gui.states;

import assets.Assets;
import assets.definitions.Definitions;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.Button;
import gui.elements.Hotbar;
import gui.elements.Statusbar;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import world.Camera;
import world.Chunk;
import world.World;

public class GameScreen extends BasicGameState {

    static StateBasedGame game;
    private static Input input;
    private boolean initialized;
    private Graphics graphics;

    private static GUI gui;
    private static boolean debugMode, showTopLayer;

    public GameScreen(int state) {
        this.initialized = false;
    }

    @Override
    public int getID() {
        return Assets.GAME_SCREEN;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        if (initialized) return;
        showTopLayer = true;
        World.init();
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

        initialized = true;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        graphics = g;
        g.setFont(Assets.FONT);
        World.draw(Window.getScale(), g, debugMode);
        gui.draw(g);

        double[] mouse_wc = Camera.getWorldCoordinates(Mouse.getX(), Mouse.getY(), Window.getScale());
        float[] mouse_osc = Camera.getOnscreenCoordinates(mouse_wc[0], mouse_wc[1], Window.getScale());
        float[] origin_osc = Camera.getOnscreenCoordinates(0, 0, Window.getScale());

        if (debugMode) {

            String[] debugStrings = new String[]{
                    World.getPlayer().getLocation().toString(),
                    World.getPlayer().getLocation().getChunk().debug(),
                    "Screen Center: "+(Window.getWidth()/2)+", "+(Window.getHeight()/2),
                    "ORIGIN: "+origin_osc[0]+", "+origin_osc[1],
                    "OSC: "+Mouse.getX()+", "+Mouse.getY()+" @ "+Window.getScale(),
                    "OSC->WC: "+mouse_wc[0]+", "+mouse_wc[1],
                    "WC->OSC: "+mouse_osc[0]+", "+mouse_osc[1]
            };

            for (int i = 0; i < debugStrings.length; i++)
                g.drawString(debugStrings[i], 10, (Window.getHeight() / 2) + (20*i));

            g.setColor(Color.red);
            g.drawLine(0, Window.getHeight()/2, Window.getWidth(), Window.getHeight()/2);
            g.setColor(Color.blue);
            g.drawLine(Window.getWidth()/2, 0, Window.getWidth()/2, Window.getHeight());
            g.setColor(Color.white);
            g.fillRect(Window.getWidth()/2 - 2, Window.getHeight()/2 - 2, 4, 4);

            //World.getPlayer().drawDebug(origin[0], origin[1], Window.getScale(), g);
        }

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
        double[] mouse_wcoords = Camera.getWorldCoordinates(x, y, Window.getScale());
        gui.onMouseRelease(x, y, button);
    }

    @Override
    public void keyPressed(int key, char c) {
        gui.onKeyDown(key);
        if (key == Input.KEY_F2)
            Window.takeScreenshot(graphics);
        if (key == Input.KEY_F3)
            debugMode = !debugMode;
        if (key == Input.KEY_F4)
            showTopLayer = !showTopLayer;
    }

    public static Input getInput() { return input; }

    public static GUI getGUI() {
        return gui;
    }

}
