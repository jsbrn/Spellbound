package gui;

import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Graphics;
import world.events.EventDispatcher;
import world.events.event.KeyDownEvent;
import world.events.event.KeyUpEvent;
import world.events.event.MouseReleaseEvent;

import java.util.ArrayList;

public final class GUI {

    private ArrayList<GUIElement> elements;

    public GUI() {
        this.elements = new ArrayList<>();
    }

    public boolean onMouseRelease(int osx, int osy) {
        for (int i = elements.size() - 1; i >= 0; i--)
            if (elements.get(i).onMouseRelease((int)(osx/Window.getScale()), (int)(osy/Window.getScale())))
                return true;
        double[] wc = MiscMath.getWorldCoordinates(osx, osy);
        EventDispatcher.invoke(new MouseReleaseEvent(wc[0], wc[1]));
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

    public void addChild(GUIElement element, int ogx, int ogy, GUIAnchor anchor) {
        element.setOffset(ogx, ogy);
        element.setAnchor(anchor);
        this.elements.add(element);
    }

    public void draw(Graphics g) { for (GUIElement element: elements) element.draw(g); }

}
