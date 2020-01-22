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

    public boolean onMousePressed(int osx, int osy, int button) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            boolean active = modal == null ? e.isActive() : (isModal(e) || isModal(e.getParent()));
            if (active && MiscMath.pointIntersectsRect(
                    osx / Window.getScale(), osy / Window.getScale(),
                    e.getCoordinates()[0],
                    e.getCoordinates()[1],
                    e.getDimensions()[0],
                    e.getDimensions()[1])) {
                if (e.onMousePressed((int)(osx/Window.getScale()), (int)(osy/Window.getScale()), button)) return true;
            }
        }
        if (modal != null) return modal.onMousePressed((int)(osx/Window.getScale()), (int)(osy/Window.getScale()), button);
        double[] wc = Camera.getWorldCoordinates(osx, osy, Window.getScale());
        EventDispatcher.invoke(new MousePressedEvent(wc[0], wc[1], button));
        return false;
    }

    public boolean onMouseRelease(int osx, int osy, int button) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            boolean active = modal == null ? e.isActive() : (isModal(e) || isModal(e.getParent()));
            if (active && MiscMath.pointIntersectsRect(
                    osx / Window.getScale(), osy / Window.getScale(),
                    e.getCoordinates()[0],
                    e.getCoordinates()[1],
                    e.getDimensions()[0],
                    e.getDimensions()[1])) {
                if (e.onMouseRelease((int)(osx/Window.getScale()), (int)(osy/Window.getScale()), button)) return true;
            }
        }
        if (modal != null) return modal.onMouseRelease((int)(osx/Window.getScale()), (int)(osy/Window.getScale()), button);
        double[] wc = Camera.getWorldCoordinates(osx, osy, Window.getScale());
        EventDispatcher.invoke(new MouseReleaseEvent(wc[0], wc[1], button));
        return false;
    }

    public boolean onKeyDown(int key) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            boolean active = modal == null ? e.isActive() : (isModal(e) || isModal(e.getParent()));
            if (active && e.onKeyDown(key))
                return true;
        }
        EventDispatcher.invoke(new KeyDownEvent(key));
        return false;
    }

    public boolean onKeyUp(int key) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            boolean active = modal == null ? e.isActive() : (isModal(e) || isModal(e.getParent()));
            if (active && e.onKeyUp(key))
                return true;
        }
        EventDispatcher.invoke(new KeyUpEvent(key));
        return false;
    }

    public void setModal(Modal element) {
        if (modal != null) modal.hide();
        modal = element;
        if (modal != null) modal.show();
    }

    public boolean isModal(GUIElement element) {
        if (element == null) return false;
        return element.equals(modal);
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

    public boolean isElementAbove(GUIElement above, GUIElement below) {
        return elements.indexOf(above) > elements.indexOf(below) || modal == null;
    }

    public void draw(Graphics g) {
        darkness = (float)MiscMath.tween(1f, darkness, 0f, 1f, 0.6f);
        if (darkness > 0) {
            g.setColor(new Color(0, 0, 0, darkness));
            g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
            g.setColor(Color.white);
        }
        for (GUIElement element: elements) if (element.isActive()) {
            element.drawUnder(g);
            element.draw(g);
            element.drawOver(g);
        }
    }

}
