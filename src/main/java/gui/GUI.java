package gui;

import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import gui.elements.Modal;
import gui.elements.PositionalTextLabel;
import gui.elements.TextLabel;
import gui.states.GameState;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import world.World;

import java.util.ArrayList;
import java.util.Stack;

public class GUI {

    private ArrayList<GUIElement> elements;
    private Stack<Modal> modals;
    private TextLabel tooltip;
    private int[] lastMousePosition;
    private boolean debugMode;
    private GameState parent;

    public GUI(GameState parent) {
        this.parent = parent;
        this.elements = new ArrayList<>();
        this.modals = new Stack<>();
        this.lastMousePosition = new int[]{0, 0};
        this.tooltip = new TextLabel("", 4,96, Integer.MAX_VALUE, Color.white, false, true);
    }

    public String getToolTipText() {
        if (!modals.isEmpty()) return modals.peek().getTooltipText();
        for (int i = elements.size() - 1; i > -1; i--) {
            String text = elements.get(i).getTooltipText();
            if (text == null) continue;
            return text;
        }
        return null;
    }

    public final boolean handleMouseMoved(int osx, int osy) {
        int ogx = (int)(osx / Window.getScale()), ogy = (int)(osy / Window.getScale());
        if (!modals.isEmpty()) return modals.peek().handleMouseMoved(ogx, ogy);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleMouseMoved(ogx, ogy)) return true;
        }
        return false;
    }

    public final boolean handleMousePressed(int osx, int osy, int button) {
        int ogx = (int)(osx / Window.getScale()), ogy = (int)(osy / Window.getScale());
        if (!modals.isEmpty()) return modals.peek().handleMousePressed(ogx, ogy, button);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleMousePressed(ogx, ogy, button)) return true;
        }
        return false;
    }

    public final boolean handleMouseRelease(int osx, int osy, int button) {
        int ogx = (int)(osx / Window.getScale()), ogy = (int)(osy / Window.getScale());
        if (!modals.isEmpty()) return modals.peek().handleMouseRelease(ogx, ogy, button);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleMouseRelease(ogx, ogy, button)) return true;
        }
        return false;
    }

    public final boolean handleMouseScroll(int direction) {
        if (!modals.isEmpty()) return modals.peek().handleMouseScroll(direction);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleMouseScroll(direction)) return true;
        }
        return false;
    }

    public boolean onKeyDown(int key) {
        if (!modals.isEmpty()) return modals.peek().handleKeyDown(key);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleKeyDown(key)) return true;
        }
        return false;
    }

    public boolean onKeyUp(int key) {
        if (!modals.isEmpty()) return modals.peek().handleKeyUp(key);
        for (int i = elements.size() - 1; i >= 0; i--) {
            GUIElement e = elements.get(i);
            if (e.handleKeyUp(key)) return true;
        }
        return false;
    }

    public void stackModal(Modal element) {
        if (element != null) {
            if (!elements.contains(element)) addElement(element, 0, 0, GUIAnchor.CENTER);
            modals.add(element);
            if (element.isActive()) element.onShow();
            element.show();
        }
    }

    public void popModal() {
        if (modals.isEmpty()) return;
        if (!modals.isEmpty()) modals.peek().hide();
        elements.remove(modals.pop());
        if (!modals.isEmpty()) modals.peek().onShow();
    }

    public void addElement(GUIElement element, int ogx, int ogy, GUIAnchor anchor) {
        element.setOffset(ogx, ogy);
        element.setAnchor(anchor);
        element.setGUI(this);
        this.elements.add(element);
    }

    public void reset() {
        modals.clear();
        elements.clear();
    }

    public void removeElement(GUIElement element) {
        if (elements.remove(element)) element.onHide();
    }

    public void floatText(Location location, String text, Color color, int speed, int lifespan, double offset, boolean randomXDirection) {
        if (location.getRegion().equals(World.getRegion()))
            addElement(
                    new PositionalTextLabel(location, text, color, randomXDirection ? MiscMath.random(-45, 45) : 0, speed, lifespan, offset),
                    -Integer.MAX_VALUE,
                    -Integer.MAX_VALUE,
                    GUIAnchor.TOP_LEFT);
    }

    public boolean isInDebugMode() { return debugMode; }

    public void toggleDebugMode() { debugMode = !debugMode; }

    public GameState getParent() {
        return parent;
    }

    public void draw(Graphics g) {

        int mouseGX = (int)(parent.getInput().getMouseX() / Window.getScale()), mouseGY = (int)(parent.getInput().getMouseY() / Window.getScale());
        if (lastMousePosition[0] != mouseGX || lastMousePosition[1] != mouseGY) {
            handleMouseMoved(parent.getInput().getMouseX(), parent.getInput().getMouseY());
            lastMousePosition[0] = mouseGX;
            lastMousePosition[1] = mouseGY;
        }

        for (int i = 0; i < elements.size(); i++) {
            if (i >= elements.size()) break;
            GUIElement element = elements.get(i);
            if (element.isActive() && !element.equals(modals)) {
                element.draw(g);
                if (debugMode) element.drawDebug(g);
            }
        }

        for (Modal modal: modals) {
            g.setColor(new Color(0, 0, 0, 0.5f));
            g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
            modal.draw(g);
            if (debugMode) modal.drawDebug(g);
        }

        String tooltipText = getToolTipText();
        tooltip.setOffset(mouseGX + 4, mouseGY);
        if (tooltipText != null) {
            tooltip.setText(tooltipText);
            tooltip.show();
        } else {
            tooltip.setText("");
            tooltip.hide();
        }

        tooltip.draw(g);

    }

}
