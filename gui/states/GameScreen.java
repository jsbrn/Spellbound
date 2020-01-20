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
import world.entities.Entity;
import world.entities.pathfinding.LocalPathFinder;
import world.entities.types.humanoids.npcs.Civilian;

import java.util.ArrayList;
import java.util.Random;

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
            @Override
            public boolean onMousePressed(int ogx, int ogy) {
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

        double[] mouse_wc = Camera.getWorldCoordinates(Mouse.getX(), Window.getHeight() - Mouse.getY(), Window.getScale());
        float[] mouse_osc = Camera.getOnscreenCoordinates(mouse_wc[0], mouse_wc[1], Window.getScale());
        float[] origin_osc = Camera.getOnscreenCoordinates(0, 0, Window.getScale());

        if (debugMode) {

            ArrayList<Entity> entities = World.getRegion().getEntities((int)mouse_wc[0] - 3, (int)mouse_wc[1] - 3, 6, 6);
            float[] osc = Camera.getOnscreenCoordinates((int)mouse_wc[0] - 3, (int)mouse_wc[1] - 3, Window.getScale());

            g.setColor(Color.black);
            g.drawRect(osc[0], osc[1], 6 * Window.getScale() * Chunk.TILE_SIZE, 6 * Window.getScale() * Chunk.TILE_SIZE);
            for (int i = 0; i < entities.size(); i++) g.drawString(entities.get(i).getClass().getSimpleName(), osc[0], osc[1] + (i * 20));


            String[] debugStrings = new String[]{
                    "Entity count: "+World.getRegion().getEntities().size(),
                    World.getPlayer().getLocation().toString(),
                    //World.getPlayer().getLocation().getChunk().debug(),
                    "Screen Center: "+(Window.getWidth()/2)+", "+(Window.getHeight()/2),
                    "ORIGIN: "+origin_osc[0]+", "+origin_osc[1],
                    "OSC: "+Mouse.getX()+", "+Mouse.getY()+" @ "+Window.getScale(),
                    "OSC->WC: "+mouse_wc[0]+", "+mouse_wc[1],
                    "WC->OSC: "+mouse_osc[0]+", "+mouse_osc[1]
            };

            ArrayList<Location> pathFromPlayer = LocalPathFinder.getPath(World.getRegion(),
                    (int)World.getPlayer().getLocation().getCoordinates()[0], (int)World.getPlayer().getLocation().getCoordinates()[1],
                    (int)mouse_wc[0], (int)mouse_wc[1]);

            g.setColor(Color.yellow);
            for (Location l: pathFromPlayer) {
                float[] losc = Camera.getOnscreenCoordinates(l.getCoordinates()[0] + 0.5, l.getCoordinates()[1] + 0.5, Window.getScale());
                g.drawRect(losc[0] - Window.getScale(), losc[1] - Window.getScale(), 2*Window.getScale(), 2*Window.getScale());
            }

            g.setColor(Color.white);
            for (int i = 0; i < debugStrings.length; i++)
                g.drawString(debugStrings[i], 10, (Window.getHeight() / 2) + (20*i));

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
        gui.onMousePressed(x, y, button);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        double[] mouse_wcoords = Camera.getWorldCoordinates(x, y, Window.getScale());
        if (button == 2) {
            Random rng = new Random();
            for (int i = 0; i < 500; i++) {
                Entity civ = new Civilian();
                Location player = World.getPlayer().getLocation();
                civ.moveTo(new Location(
                        player.getRegion(),
                        mouse_wcoords[0] + -8 + rng.nextInt(16),
                        mouse_wcoords[1] + -8 + rng.nextInt(16)));
                if (rng.nextBoolean()) civ.enterState("idle");
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

}
