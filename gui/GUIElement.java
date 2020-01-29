package gui;

import gui.elements.SpeechBubble;
import gui.states.GameScreen;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.*;

import java.util.ArrayList;

public abstract class GUIElement {

    private double[] offset;
    private GUI gui;
    private GUIAnchor anchor = GUIAnchor.TOP_LEFT;
    private GUIElement parent;
    private Image buffer;
    private boolean buffered = true;
    private ArrayList<GUIElement> children = new ArrayList<>();
    private boolean inactive;

    public final void setAnchor(GUIAnchor anchor) { this.anchor = anchor; }
    public final void setOffset(double gx, double gy) { offset = new double[]{gx, gy}; }
    public abstract int[] getDimensions();
    public final double[] getCoordinates() {
        int[] dims = getDimensions();
        double[] p_coords = parent != null ? parent.getCoordinates() : new double[]{0, 0};
        int[] p_dims = parent != null ? parent.getDimensions() : new int[]{
                (int)(Window.getWidth() / Window.getScale()),
                (int)(Window.getHeight() / Window.getScale())
        };

        switch(anchor) {
            case TOP_LEFT: return new double[]{ p_coords[0] + offset[0], p_coords[1] + offset[1] };
            case TOP_MIDDLE: return new double[]{ p_coords[0] + ((double)p_dims[0]/2f) - (dims[0]/2f) + offset[0], p_coords[1] + offset[1] };
            case TOP_RIGHT: return new double[]{ p_coords[0] + p_dims[0] - dims[0] + offset[0], p_coords[1] + offset[1]};
            case LEFT_MIDDLE: return new double[]{ p_coords[0] + offset[0], p_coords[1] + ((double)p_dims[1]/2) - ((double)dims[1]/2) + offset[1] };
            case CENTER: return new double[]{ p_coords[0] + ((double)p_dims[0]/2f) - ((double)dims[0] / 2f) + offset[0], p_coords[1] + ((double)p_dims[1]/2f) - ((double)dims[1]/2f) + offset[1] };
            case RIGHT_MIDDLE: return new double[]{ p_coords[0] - dims[0] + offset[0], p_coords[1] + ((double)p_dims[1]/2) - ((double)dims[1]/2) + offset[1] };
            case BOTTOM_LEFT: return new double[]{ p_coords[0] + offset[0], p_coords[1] + p_dims[1] - dims[1] + offset[1] };
            case BOTTOM_MIDDLE: return new double[]{ p_coords[0] + ((double)p_dims[0]/2f) - ((double)dims[0]/2) + offset[0], p_coords[1] + p_dims[1] + offset[1] - dims[1] };
            case BOTTOM_RIGHT: return new double[]{ p_coords[0] + p_dims[0] - dims[0] + offset[0], p_coords[1] + p_dims[1] + offset[1] - dims[1] };
            default: return new double[]{0, 0};
        }
    }

    public final float[] getOnscreenCoordinates() {
        return new float[]{(float)getCoordinates()[0] * Window.getScale(), (float)getCoordinates()[1] * Window.getScale()};
    }

    public final void setParent(GUIElement parent) { this.parent = parent; }
    public final GUIElement getParent() { return parent; }

    public final void setGUI(GUI parent) { this.gui = parent; }
    public final GUI getGUI() { return parent != null ? parent.getGUI() : gui; }

    public boolean isActive() { return parent != null ? parent.isActive() : !inactive; }
    public void show() { inactive = false; }
    public void hide() { inactive = true; }

    public boolean mouseIntersects() {
        return MiscMath.pointIntersectsRect(
                GameScreen.getInput().getMouseX() / Window.getScale(), GameScreen.getInput().getMouseY() / Window.getScale(),
                getCoordinates()[0],
                getCoordinates()[1],
                getDimensions()[0],
                getDimensions()[1]);
    }

    public final boolean handleMousePressed(int osx, int osy, int button) {
        for (int i = children.size() - 1; i >= 0; i--) {
            GUIElement e = children.get(i);
            if (e.handleMousePressed(osx, osy, button)) return true;
        }
        if (isActive()) {
            return onMousePressed(osx, osy, button);
        } else { return false; }
    }

    public final boolean handleMouseRelease(int osx, int osy, int button) {
        for (int i = children.size() - 1; i >= 0; i--) {
            GUIElement e = children.get(i);
            if (e.handleMouseRelease(osx, osy, button)) return true;
        }
        if (isActive()) {
            return onMouseRelease(osx, osy, button);
        } else { return false; }
    }

    public final boolean handleKeyUp(int key) {
        for (int i = children.size() - 1; i >= 0; i--) {
            GUIElement e = children.get(i);
            if (e.isActive())
                if (e.handleKeyUp(key)) return true;
        }
        return isActive() && onKeyUp(key);
    }

    public final boolean handleKeyDown(int key) {
        for (int i = children.size() - 1; i >= 0; i--) {
            GUIElement e = children.get(i);
            if (e.isActive())
                if (e.handleKeyDown(key)) return true;
        }
        return isActive() && onKeyDown(key);
    }

    public abstract boolean onMouseRelease(int ogx, int ogy, int button);
    public abstract boolean onMousePressed(int ogx, int ogy, int button);
    public abstract boolean onKeyDown(int key);
    public abstract boolean onKeyUp(int key);

    public final GUIElement addChild(GUIElement element, int ogx, int ogy, GUIAnchor anchor) {
        children.add(element);
        element.setParent(this);
        element.setOffset(ogx, ogy);
        element.setAnchor(anchor);
        return this;
    }

    public final void removeChild(GUIElement element) {
        children.remove(element);
    }

    public final void draw(Graphics g) {
        drawUnder(g);
        try {
            int[] dimensions = getDimensions();
            float[] coordinates = getOnscreenCoordinates();
            if (buffered) {
                if (buffer == null) buffer = new Image((int)dimensions[0], (int)dimensions[1]);
                drawBuffered(buffer.getGraphics(),
                        mouseIntersects(),
                        GameScreen.getInput().isMouseButtonDown(0));
                g.drawImage(buffer.getScaledCopy(Window.getScale()),coordinates[0], coordinates[1]);
            }
        } catch (SlickException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < children.size(); i++) children.get(i).draw(g);
        drawOver(g);
    }

    public void setBuffered(boolean b) { buffered = b; }

    public void drawUnder(Graphics g) {}
    public void drawOver(Graphics g) {}

    protected abstract void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown);

    protected final void drawDebug(Graphics g) {
        g.setColor(Color.magenta);
        g.drawRect(
                getOnscreenCoordinates()[0],
                getOnscreenCoordinates()[1],
                getDimensions()[0] * Window.getScale(),
                getDimensions()[1] * Window.getScale());
        for (GUIElement child: children) child.drawDebug(g);
        g.setColor(Color.white);
    }


}
