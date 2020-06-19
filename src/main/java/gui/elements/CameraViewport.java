package gui.elements;

import assets.Assets;
import gui.GUIElement;
import gui.menus.CheatCodeMenu;
import misc.MiscMath;
import misc.Window;
import org.lwjgl.input.Mouse;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Input;
import world.Camera;
import world.Chunk;
import world.World;
import world.entities.Entity;
import world.events.EventDispatcher;
import world.events.event.KeyDownEvent;
import world.events.event.KeyUpEvent;
import world.events.event.MousePressedEvent;
import world.events.event.MouseReleaseEvent;

import java.util.ArrayList;

public class CameraViewport extends GUIElement {

    @Override
    public int[] getDimensions() {
        return new int[]{(int)(Window.getScreenWidth() / Window.getScale()), (int)(Window.getScreenHeight() / Window.getScale())};
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        double[] wc = Camera.getWorldCoordinates(ogx * Window.getScale(), ogy * Window.getScale(), Window.getScale());
        EventDispatcher.invoke(new MouseReleaseEvent(wc[0], wc[1], button));
        return true;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) {
        double[] wc = Camera.getWorldCoordinates(ogx * Window.getScale(), ogy * Window.getScale(), Window.getScale());
        EventDispatcher.invoke(new MousePressedEvent(wc[0], wc[1], button));
        return true;
    }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public boolean onKeyDown(int key) {
        EventDispatcher.invoke(new KeyDownEvent(key));
        return true;
    }

    @Override
    public boolean onKeyUp(int key) {
        if (key == Input.KEY_F3) getGUI().toggleDebugMode();
        if (key == Input.KEY_F12) getGUI().stackModal(new CheatCodeMenu());
        EventDispatcher.invoke(new KeyUpEvent(key));
        return true;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {

    }

    @Override
    public void drawUnder(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
    }

    @Override
    public void drawOver(Graphics g) {
        if (World.exists()) {
            World.draw(Window.getScale(), g);
        }
    }

    @Override
    protected void drawDebug(Graphics g) {
        super.drawDebug(g);

        if (!World.exists()) return;

        double[] mouse_wc = Camera.getWorldCoordinates(Mouse.getX(), Window.getHeight() - Mouse.getY(), Window.getScale());
        float[] mouse_osc = Camera.getOnscreenCoordinates(mouse_wc[0], mouse_wc[1], Window.getScale());
        float[] origin_osc = Camera.getOnscreenCoordinates(0, 0, Window.getScale());
        ArrayList<Entity> entities = World.getRegion().getEntities((int)mouse_wc[0], (int)mouse_wc[1], 1, 1);
        float[] osc = Camera.getOnscreenCoordinates((int)mouse_wc[0], (int)mouse_wc[1], Window.getScale());

        World.getRegion().drawDebug(Window.getScale(), g);

        g.setFont(Assets.getFont(14));

        g.setColor(Color.black);
        g.drawRect(osc[0], osc[1], 1 * Window.getScale() * Chunk.TILE_SIZE, 1 * Window.getScale() * Chunk.TILE_SIZE);
        for (int i = 0; i < entities.size(); i++) g.drawString(entities.get(i).getClass().getSimpleName(), osc[0], osc[1] + (i * 20));

        String[] debugStrings = new String[]{
                "FPS: "+ Window.WINDOW_INSTANCE.getFPS(),
                "Entity count: "+World.getRegion().getEntities().size(),
                "Region: "+World.getRegion().getName(),
                "Coordinates: "+MiscMath.round(World.getLocalPlayer().getLocation().getCoordinates()[0], 0.25)
                        +", "+MiscMath.round(World.getLocalPlayer().getLocation().getCoordinates()[1], 0.25)
        };

        g.setColor(Color.white);
        for (int i = debugStrings.length - 1; i > -1; i--)
            g.drawString(debugStrings[i], 10, (Window.getHeight()) - (20*((debugStrings.length-1-i)+1)));

    }
}
