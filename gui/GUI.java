package gui;

import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Camera;
import world.events.EventDispatcher;
import world.events.event.KeyDownEvent;
import world.events.event.KeyUpEvent;
import world.events.event.MousePressedEvent;
import world.events.event.MouseReleaseEvent;

import java.util.ArrayList;

public final class GUI {

    private ArrayList<GUIElement> elements;
    private float darkness;

    public GUI() {
        this.elements = new ArrayList<>();
    }

    public boolean onMousePressed(int osx, int osy, int button) {
        for (int i = elements.size() - 1; i >= 0; i--)
            if (elements.get(i).onMousePressed((int)(osx/Window.getScale()), (int)(osy/Window.getScale())))
                return true;
        double[] wc = Camera.getWorldCoordinates(osx, osy, Window.getScale());
        EventDispatcher.invoke(new MousePressedEvent(wc[0], wc[1], button));
        return false;
    }

    public boolean onMouseRelease(int osx, int osy, int button) {
        for (int i = elements.size() - 1; i >= 0; i--)
            if (elements.get(i).onMouseRelease((int)(osx/Window.getScale()), (int)(osy/Window.getScale())))
                return true;
        double[] wc = Camera.getWorldCoordinates(osx, osy, Window.getScale());
        EventDispatcher.invoke(new MouseReleaseEvent(wc[0], wc[1], button));
        return false;
    }

    public boolean onKeyDown(int key) {
        for (int i = elements.size() - 1; i >= 0; i--)
            if (elements.get(i).onKeyDown(key))
                return true;
        EventDispatcher.invoke(new KeyDownEvent(key));
        return false;
    }

    public boolean onKeyUp(int key) {
        for (int i = elements.size() - 1; i >= 0; i--)
            if (elements.get(i).onKeyUp(key))
                return true;
        EventDispatcher.invoke(new KeyUpEvent(key));
        return false;
    }

    public void setFade(float alpha) {
        darkness = alpha;
    }

    public void addChild(GUIElement element, int ogx, int ogy, GUIAnchor anchor) {
        element.setOffset(ogx, ogy);
        element.setAnchor(anchor);
        this.elements.add(element);
    }

    public void draw(Graphics g) {
        darkness = (float)MiscMath.tween(1f, darkness, 0f, 1f, 0.6f);
        if (darkness > 0) {
            g.setColor(new Color(0, 0, 0, darkness));
            g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
            g.setColor(Color.white);
        }
        for (GUIElement element: elements) element.draw(g);
    }

}
