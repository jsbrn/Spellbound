package gui.elements;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Input;
import gui.GUIElement;
import gui.menus.CheatCodeMenu;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import network.MPClient;
import network.MPServer;
import org.lwjgl.input.Mouse;
import world.Camera;
import world.Chunk;
import world.entities.components.HealthComponent;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

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
        //EventManager.invoke(new MouseReleaseEvent(wc[0], wc[1], button));
        //TODO: send packet instead
        return true;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) {
        double[] wc = Camera.getWorldCoordinates(ogx * Window.getScale(), ogy * Window.getScale(), Window.getScale());
        //EventManager.invoke(new MousePressedEvent(wc[0], wc[1], button));
        //TODO: send packet instead
        return true;
    }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public boolean onKeyDown(int key) {
        //EventManager.invoke(new KeyDownEvent(key));
        //TODO: send packet instead
        return true;
    }

    @Override
    public boolean onKeyUp(int key) {
        if (key == Input.KEY_F3) getGUI().toggleDebugMode();
        if (key == Input.KEY_F12) getGUI().stackModal(new CheatCodeMenu());
        if (key == Input.KEY_T) {
            ((VelocityComponent)MPServer.getWorld().getEntities().getComponent(VelocityComponent.class, Camera.getTargetEntity()))
                    .addForce(Math.random() * 360, 1 + (Math.random() * 4), 2);
        }
        //Host.getEventManager().invoke(new KeyUpEvent(key));
        //TODO: send key packet to server from client, don't trigger an event
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
        if (Camera.getTargetEntity() == null) return;
        MPClient.getWorld().getRegion(Camera.getLocation()).draw(Window.getScale(), g);
    }

    @Override
    protected void drawDebug(Graphics g) {
        super.drawDebug(g);

        double[] mouse_wc = Camera.getWorldCoordinates(Mouse.getX(), Window.getHeight() - Mouse.getY(), Window.getScale());
        float[] mouse_osc = Camera.getOnscreenCoordinates(mouse_wc[0], mouse_wc[1], Window.getScale());
        float[] origin_osc = Camera.getOnscreenCoordinates(0, 0, Window.getScale());
        ArrayList<Integer> entities = MPClient.getWorld().getRegion(Camera.getLocation()).getEntityIDs((int)mouse_wc[0], (int)mouse_wc[1], 1, 1);
        float[] osc = Camera.getOnscreenCoordinates((int)mouse_wc[0], (int)mouse_wc[1], Window.getScale());

        MPClient.getWorld().getRegion(Camera.getLocation()).drawDebug(Window.getScale(), g);

        g.setFont(Assets.getFont(14));

        g.setColor(Color.black);
        g.drawRect(osc[0], osc[1], 1 * Window.getScale() * Chunk.TILE_SIZE, 1 * Window.getScale() * Chunk.TILE_SIZE);
        for (int i = 0; i < entities.size(); i++) g.drawString("Entity #"+entities.get(i), osc[0], osc[1] + (i * 20));

        Location localPlayerLocation = ((LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, Camera.getTargetEntity())).getLocation();
        Location serverPlayerLocation = ((LocationComponent)MPServer.getWorld().getEntities().getComponent(LocationComponent.class, Camera.getTargetEntity())).getLocation();

        String[] debugStrings = new String[]{
                "FPS: "+ Window.WINDOW_INSTANCE.getFPS(),
                "Ping: "+MPClient.getPing()+"ms",
                "Entity count: "+MPClient.getWorld().getRegion(Camera.getLocation()).getEntityIDs().size(),
                "Mouse (WC): "+mouse_wc[0]+", "+mouse_wc[1],
                "Region: "+MPClient.getWorld().getRegion(Camera.getLocation()).getName(),
                "Coordinates: "+MiscMath.round(localPlayerLocation.getCoordinates()[0], 0.25)
                        +", "+MiscMath.round(localPlayerLocation.getCoordinates()[1], 0.25),
                "Distance to server copy: "+localPlayerLocation.distanceTo(serverPlayerLocation)+" tiles"
        };

        g.setColor(Color.white);
        for (int i = debugStrings.length - 1; i > -1; i--)
            g.drawString(debugStrings[i], 10, (Window.getHeight()) - (20*((debugStrings.length-1-i)+1)));

    }
}
