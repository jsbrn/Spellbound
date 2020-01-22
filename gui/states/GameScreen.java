package gui.states;

import assets.Assets;
import assets.definitions.Definitions;
import gui.GUI;
import gui.GUIAnchor;
import gui.elements.*;
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
import world.entities.Entity;
import world.entities.states.PatrolState;
import world.entities.types.humanoids.npcs.Civilian;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen extends BasicGameState {

    static StateBasedGame game;
    private static Input input;
    private static boolean initialized, paused;
    private Graphics graphics;

    private static GUI gui;
    private static Modal spellbook;
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

        spellbook = new Modal("spellbook_bg.png");

        Button b = new Button(32, 8) {
            @Override
            public boolean onMousePressed(int ogx, int ogy, int button) {
                System.out.println("OK COOL!");
                return true;
            }
        };
        gui.addElement(new Statusbar(World.getPlayer()), 2, 2, GUIAnchor.TOP_LEFT);
        gui.addElement(new Hotbar(World.getPlayer()), 2, 38, GUIAnchor.TOP_LEFT);

        gui.addElement(new Button("spellbook.png") {
            @Override
            public boolean onKeyUp(int key) {
                if (key == Input.KEY_TAB) {
                    gui.setModal(spellbook);
                    paused = true;
                    return true;
                }
                return false;
            }
            @Override
            public boolean onMousePressed(int ogx, int ogy, int button) {
                gui.setModal(spellbook);
                System.out.println("OK COOL THEN");
                paused = true;
                return true;
            }
        }, 4, 94, GUIAnchor.TOP_LEFT);

        gui.addElement(spellbook, 0, 0, GUIAnchor.CENTER);
        gui.addElement(b, 8, 12, GUIAnchor.TOP_LEFT);
        b.setParent(spellbook);

        Label title = new Label("Create a spell", 5, Color.black);
        gui.addElement(title, 12, 4, GUIAnchor.TOP_LEFT);
        title.setParent(spellbook);

        Assets.load();
        Definitions.load();

        initialized = true;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        graphics = g;
        g.setFont(Assets.getFont(14));
        World.draw(Window.getScale(), g, debugMode);
        gui.draw(g);

        double[] mouse_wc = Camera.getWorldCoordinates(Mouse.getX(), Window.getHeight() - Mouse.getY(), Window.getScale());
        float[] mouse_osc = Camera.getOnscreenCoordinates(mouse_wc[0], mouse_wc[1], Window.getScale());
        float[] origin_osc = Camera.getOnscreenCoordinates(0, 0, Window.getScale());

        if (debugMode) {

            ArrayList<Entity> entities = World.getRegion().getEntities((int)mouse_wc[0], (int)mouse_wc[1], 1, 1);
            float[] osc = Camera.getOnscreenCoordinates((int)mouse_wc[0], (int)mouse_wc[1], Window.getScale());

            g.setColor(Color.black);
            g.drawRect(osc[0], osc[1], 1 * Window.getScale() * Chunk.TILE_SIZE, 1 * Window.getScale() * Chunk.TILE_SIZE);
            for (int i = 0; i < entities.size(); i++) g.drawString(entities.get(i).getClass().getSimpleName(), osc[0], osc[1] + (i * 20));


            String[] debugStrings = new String[]{
                    "Entity count: "+World.getRegion().getEntities().size(),
                    World.getPlayer().getLocation().toString(),
                    //World.getPlayer().getLocation().getChunk().debug(),
                    "Screen Center: "+(Window.getWidth()/2)+", "+(Window.getHeight()/2),
                    "ORIGIN: "+origin_osc[0]+", "+origin_osc[1],
                    "OSC: "+Mouse.getX()+", "+Mouse.getY()+" @ "+Window.getScale(),
                    "OSC->WC: "+mouse_wc[0]+", "+mouse_wc[1],
                    "WC->OSC: "+mouse_osc[0]+", "+mouse_osc[1],
                    "Player can see: "+World.getPlayer().canSee((int)mouse_wc[0], (int)mouse_wc[1])
            };

            g.setColor(Color.white);
            for (int i = 0; i < debugStrings.length; i++)
                g.drawString(debugStrings[i], 10, (Window.getHeight() / 2) + (20*i));

        }

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        MiscMath.DELTA_TIME = delta;
        input = gc.getInput();
        if (!paused) World.update();

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
        gui.onMousePressed(x, y, button);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        double[] mouse_wcoords = Camera.getWorldCoordinates(x, y, Window.getScale());
        if (button == 2) {
            Random rng = new Random();
            for (int i = 0; i < 100; i++) {
                Entity civ = new Civilian();
                Location player = World.getPlayer().getLocation();
                civ.moveTo(new Location(
                        player.getRegion(),
                        mouse_wcoords[0],
                        mouse_wcoords[1]));
                civ.enterState(new PatrolState(World.getPlayer(), 4));
            }
        }
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

    public static void setPaused(boolean p) { paused = p; }

}
