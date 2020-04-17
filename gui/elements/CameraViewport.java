package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import gui.menus.PauseMenu;
import misc.Location;
import misc.Window;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import world.Camera;
import world.Chunk;
import world.World;
import world.entities.Entity;
import world.entities.types.humanoids.npcs.LostCivilian;
import world.events.EventDispatcher;
import world.events.event.KeyDownEvent;
import world.events.event.KeyUpEvent;
import world.events.event.MousePressedEvent;
import world.events.event.MouseReleaseEvent;

import java.util.ArrayList;
import java.util.Random;

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
        double[] mouse_wcoords = Camera.getWorldCoordinates(ogx * Window.getScale(), ogy * Window.getScale(), Window.getScale());
        if (button == 2) {
            Random rng = new Random();
            for (int i = 0; i < 1; i++) {
                Entity civ = new LostCivilian(1);
                Location player = World.getLocalPlayer().getLocation();
                civ.moveTo(new Location(
                        player.getRegion(),
                        mouse_wcoords[0],
                        mouse_wcoords[1]));
            }
        }
        if (button == Input.MOUSE_RIGHT_BUTTON) World.getLocalPlayer().activateGodMode();
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
        EventDispatcher.invoke(new KeyUpEvent(key));
        return true;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {

    }

    @Override
    public void drawOver(Graphics g) {
        if (World.exists()) {
            World.draw(Window.getScale(), g, getGUI().isInDebugMode());
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
