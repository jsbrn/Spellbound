package gui;

import gui.elements.Modal;
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
    private Modal modal;
    private float darkness;

    public GUI() {
        this.elements = new ArrayList<>();
    }

    public boolean handleMousePressed(int osx, int osy, int button) {
        if (modal != null) return modal.handleMousePressed(osx, osy, button);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleMousePressed(osx, osy, button)) return true;
        }
        double[] wc = Camera.getWorldCoordinates(osx, osy, Window.getScale());
        EventDispatcher.invoke(new MousePressedEvent(wc[0], wc[1], button));
        return false;
    }

    public boolean handleMouseRelease(int osx, int osy, int button) {
        if (modal != null) return modal.handleMouseRelease(osx, osy, button);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleMouseRelease(osx, osy, button)) return true;
        }
        double[] wc = Camera.getWorldCoordinates(osx, osy, Window.getScale());
        EventDispatcher.invoke(new MouseReleaseEvent(wc[0], wc[1], button));
        return false;
    }

    public boolean onKeyDown(int key) {
        if (modal != null) return modal.handleKeyDown(key);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleKeyDown(key)) return true;
        }
        EventDispatcher.invoke(new KeyDownEvent(key));
        return false;
    }

    public boolean onKeyUp(int key) {
        if (modal != null) return modal.handleKeyUp(key);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleKeyUp(key)) return true;
        }
        EventDispatcher.invoke(new KeyUpEvent(key));
        return false;
    }

    public void setModal(Modal element) {
        if (modal != null) modal.hide();
        modal = element;
        if (modal != null) modal.show();
    }

    public void setFade(float alpha) {
        darkness = alpha;
    }

    public void addElement(GUIElement element, int ogx, int ogy, GUIAnchor anchor) {
        element.setOffset(ogx, ogy);
        element.setAnchor(anchor);
        element.setGUI(this);
        this.elements.add(element);
    }

    public void draw(Graphics g, boolean debug) {

        darkness = (float)MiscMath.tween(1f, darkness, 0f, 1f, 0.6f);
        if (darkness > 0) {
            g.setColor(new Color(0, 0, 0, darkness));
            g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
            g.setColor(Color.white);
        }

        for (GUIElement element: elements) {
            if (element.isActive() && !element.equals(modal)) {
                element.draw(g);
                if (debug) element.drawDebug(g);
            }
        }

        if (modal != null) {
            g.setColor(new Color(0, 0, 0, 0.5f));
            g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
            modal.draw(g);
            if (debug) modal.drawDebug(g);
        }

    }

}
