package gui;

import misc.Window;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public final class GUI {

    private ArrayList<GUIElement> elements;

    public GUI() {
        this.elements = new ArrayList<>();
    }

    public boolean sendClick(int osx, int osy) {
        for (int i = elements.size() - 1; i >= 0; i--)
            if (elements.get(i).onClick((int)(osx/Window.getScale()), (int)(osy/Window.getScale())))
                return true;
        return false;
    }

    public boolean sendKeyStroke(int key) {
        for (int i = elements.size() - 1; i >= 0; i--)
            if (elements.get(i).onKeyPress(key))
                return true;
        return false;
    }

    public void addChild(GUIElement element, int ogx, int ogy, GUIAnchor anchor) {
        element.setOffset(ogx, ogy);
        element.setAnchor(anchor);
        this.elements.add(element);
    }

    public void draw(Graphics g) { for (GUIElement element: elements) element.draw(g); }

}
