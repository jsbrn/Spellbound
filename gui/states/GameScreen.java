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
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import world.Chunk;
import world.World;

public class GameScreen extends BasicGameState {

    static StateBasedGame game;
    private static Input input;
    private boolean initialized;
    private Graphics graphics;

    private static GUI gui;
    private static boolean debugMode;

    public GameScreen(int state) {
        this.initialized = false;
    }

    @Override
    public int getID() {
        return Assets.GAME_SCREEN;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        if (initialized) return;
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
        float[] origin = MiscMath.getWorldOnscreenOrigin();
        World.draw(origin[0], origin[1], Window.getScale(), g);
        gui.draw(g);

        if (debugMode) {
            for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
                for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
                    float tile_osw = Chunk.TILE_SIZE * Window.getScale();
                    g.drawRect(origin[0] + (i * tile_osw), origin[1] + (j * tile_osw), tile_osw, tile_osw);
                }
            }
            int y = Window.getHeight()/2;
            g.drawString(World.getPlayer().debug(), 10, y);
            g.drawString(World.getPlayer().getLocation().toString(), 10, y+25);
            g.drawString(World.getPlayer().getLocation().getChunk().debug(), 10, y+50);
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
        double[] mouse_wcoords = MiscMath.getWorldCoordinates(x, y);
        gui.onMouseRelease(x, y, button);
    }

    @Override
    public void keyPressed(int key, char c) {
        gui.onKeyDown(key);
        if (key == Input.KEY_F2)
            Window.takeScreenshot(graphics);
        if (key == Input.KEY_F3)
            debugMode = !debugMode;
        if (key == Input.KEY_T)
            World.getPlayer().moveTo(new Location(World.getRegion("player_home"), World.getRegion().getChunk(0, 0), 0, 0));
    }

    public static Input getInput() { return input; }

    public static boolean debugModeEnabled() { return debugMode; }

    public static GUI getGUI() {
        return gui;
    }

}
