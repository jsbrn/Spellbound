package gui.states;

import assets.Assets;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import world.Chunk;
import world.World;
import world.entities.actions.action.SetAnimationAction;
import world.particles.ParticleSource;

public class GameScreen extends BasicGameState {

    static StateBasedGame game;
    private Input input;
    private boolean init;

    private ParticleSource particleSource;

    private Image wood_bg, frame, cursor;

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
        Assets.loadTileSprite();
        World.init(16);

        this.particleSource = new ParticleSource();

        wood_bg = new Image("assets/wood.png", false, Image.FILTER_NEAREST);
        frame = new Image("assets/frame.png", false, Image.FILTER_NEAREST);
        cursor = new Image("assets/cursor.png", false, Image.FILTER_NEAREST);
        init = true;
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        float scale = (Window.getHeight() / (Chunk.CHUNK_SIZE * Chunk.TILE_SIZE)) - 1;
        float size = scale * Chunk.TILE_SIZE * Chunk.CHUNK_SIZE;
        float ox = (Window.getWidth()/2) - (size / 2);
        float oy = (Window.getHeight() / 2) - (size/2);

//        wood_bg.startUse();
//        for (int i = 0; i < Window.getScreenHeight() / wood_bg.getWidth() * scale; i++) {
//            for (int j = 0; j < Window.getScreenHeight() / wood_bg.getHeight(); j++) {
//                wood_bg.drawEmbedded(i * wood_bg.getWidth() * scale, j * wood_bg.getHeight() * scale, wood_bg.getWidth() * scale, wood_bg.getHeight() * scale);
//            }
//        }
//        wood_bg.endUse();
//        frame.draw(ox - (6 * scale), oy - (6 * scale), scale);

        World.draw(ox, oy, scale, g);

        this.particleSource.draw(ox, oy, scale, g);
        this.particleSource.setCoordinates(
                (gc.getInput().getMouseX() - ox) / scale / Chunk.TILE_SIZE,
                (gc.getInput().getMouseY() - oy) / scale / Chunk.TILE_SIZE);

        g.drawString(particleSource.debug(), 0, 70);

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

        this.particleSource.update();

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
    public void keyPressed(int key, char c) {

        if (World.getPlayer().getActionQueue().isEmpty()) {
            if (key == Input.KEY_C) World.getPlayer().setAnimation("casting");
        }
    }

}
