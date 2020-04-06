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
import world.Region;
import world.World;
import world.entities.Entity;
import world.entities.types.humanoids.npcs.Civilian;
import world.generators.region.DungeonGenerator;

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

    private static MiniMap miniMap;

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

        World.getLocalPlayer().getSpellbook().discoverAllTechniques();

        SpellcraftingMenu spellcasting = new SpellcraftingMenu(World.getLocalPlayer());
        spellbook = new Journal(World.getLocalPlayer(), spellcasting);

        gui.addElement(new Statusbar(World.getLocalPlayer()), 2, 2, GUIAnchor.TOP_LEFT);
        gui.addElement(new Hotbar(World.getLocalPlayer()), 2, 38, GUIAnchor.TOP_LEFT);
        gui.addElement(new Button(null, 16, 16, "spellbook.png", false) {
            @Override
            public boolean onKeyDown(int key) {
                if (key == Input.KEY_TAB) {
                    gui.stackModal(spellbook);
                    World.setPaused(true);
                    return true;
                }
                return false;
            }
            @Override
            public boolean onClick(int button) {
                gui.stackModal(spellbook);
                World.setPaused(true);
                return true;
            }
        }, 4, 94, GUIAnchor.TOP_LEFT);

        miniMap = new MiniMap();
        gui.addElement(miniMap, -2, 2, GUIAnchor.TOP_RIGHT);

        gui.addElement(spellbook, 0, 0, GUIAnchor.CENTER);
        gui.addElement(spellcasting, 0, 0, GUIAnchor.CENTER);
        spellbook.hide();
        spellcasting.hide();

        gui.setSpeechBubble();

        Assets.load();
        Definitions.load();

        initialized = true;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        graphics = g;
        input = gc.getInput();
        g.setFont(Assets.getFont(14));
        World.draw(Window.getScale(), g, debugMode);
        gui.draw(g, debugMode);

        g.setFont(Assets.getFont(14));

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
                    World.getLocalPlayer().getLocation().toString(),
                    //World.getPlayer().getLocation().getChunk().debug(),
                    "Screen Center: "+(Window.getWidth()/2)+", "+(Window.getHeight()/2),
                    "ORIGIN: "+origin_osc[0]+", "+origin_osc[1],
                    "OSC: "+Mouse.getX()+", "+Mouse.getY()+" @ "+Window.getScale(),
                    "OSC->WC: "+mouse_wc[0]+", "+mouse_wc[1],
                    "WC->OSC: "+mouse_osc[0]+", "+mouse_osc[1],
                    "Player can see: "+World.getLocalPlayer().canSee((int)mouse_wc[0], (int)mouse_wc[1])
            };

            g.setColor(Color.white);
            for (int i = 0; i < debugStrings.length; i++)
                g.drawString(debugStrings[i], 10, (Window.getHeight() / 2) + (20*i));

        }

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        World.setTimeMultiplier(gc.getInput().isKeyDown(Input.KEY_LSHIFT) ? 0.5 : 1);
        MiscMath.DELTA_TIME = (int)(delta * World.getTimeMultiplier());
        input = gc.getInput();
        if (!paused) World.update();

    }

    @Override
    public void keyReleased(int key, char c) {

        if (key == Input.KEY_F5) miniMap.setRegion(new Region("test_dungeon", 16, new DungeonGenerator(16*4, 12)));
        if (key == Input.KEY_F6) miniMap.setRegion(null);

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
        double[] mouse_wcoords = Camera.getWorldCoordinates(x, y, Window.getScale());
        if (button == 2) {
            Random rng = new Random();
            for (int i = 0; i < 1; i++) {
                Entity civ = new Civilian();
                Location player = World.getLocalPlayer().getLocation();
                civ.moveTo(new Location(
                        player.getRegion(),
                        mouse_wcoords[0],
                        mouse_wcoords[1]));
            }
        }
        gui.handleMouseRelease(x, y, button);
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
