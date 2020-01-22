package gui;

import misc.Window;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;

public abstract class GUIElement {

    private int[] offset;
    private GUI gui;
    private GUIAnchor anchor = GUIAnchor.TOP_LEFT;
    private GUIElement parent;
    private Image buffer;
    private boolean inactive;

    public final void setAnchor(GUIAnchor anchor) { this.anchor = anchor; }
    public final void setOffset(int gx, int gy) { offset = new int[]{gx, gy}; }
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
            case TOP_MIDDLE: return new double[]{ p_coords[0] + ((double)p_dims[0]/2f) - (dims[0] - 2) + offset[0], p_coords[1] + offset[1] };
            case TOP_RIGHT: return new double[]{ p_coords[0] + p_dims[0] - dims[0] - offset[0], p_coords[1] + offset[1]};
            case LEFT_MIDDLE: return new double[]{ p_coords[0] + offset[0], p_coords[1] + ((double)p_dims[1]/2) - ((double)dims[1]/2) + offset[1] };
            case CENTER: return new double[]{ p_coords[0] + ((double)p_dims[0]/2f) - ((double)dims[0] / 2f) + offset[0], p_coords[1] + ((double)p_dims[1]/2f) - ((double)dims[1]/2f) + offset[1] };
            case RIGHT_MIDDLE: return new double[]{ p_coords[0] - dims[0] + offset[0], p_coords[1] + ((double)p_dims[1]/2) - ((double)dims[1]/2) + offset[1] };
            case BOTTOM_LEFT: return new double[]{ p_coords[0] + offset[0], p_coords[1] + p_dims[1] + offset[1] };
            case BOTTOM_MIDDLE: return new double[]{ p_coords[0] + ((double)p_dims[0]/2f) - (dims[0] - 2) + offset[0], p_coords[1] + p_dims[1] + offset[1] };
            case BOTTOM_RIGHT: return new double[]{ p_coords[0] + p_dims[0] - dims[0] - offset[0], p_coords[1] + p_dims[1] + offset[1] };
            default: return new double[]{0, 0};
        }
    }

    public final float[] getOnscreenCoordinates() {
        return new float[]{(float)getCoordinates()[0] * Window.getScale(), (float)getCoordinates()[1] * Window.getScale()};
    }

    public final void setParent(GUIElement parent) { this.parent = parent; }
    public final GUIElement getParent() { return parent; }

    public final void setGUI(GUI parent) { this.gui = parent; }
    public final GUI getGUI() { return gui; }

    public boolean isActive() { return parent != null ? parent.isActive() : !inactive; }
    public void show() { inactive = false; }
    public void hide() { inactive = true; }

    public abstract boolean onMouseRelease(int ogx, int ogy, int button);
    public abstract boolean onMousePressed(int ogx, int ogy, int button);
    public abstract boolean onKeyDown(int key);
    public abstract boolean onKeyUp(int key);

    public final void draw(Graphics g) {
        try {
            int[] dimensions = getDimensions();
            float[] coordinates = getOnscreenCoordinates();
            if (buffer == null) buffer = new Image((int)dimensions[0], (int)dimensions[1]);
            drawBuffered(buffer.getGraphics());
            g.drawImage(buffer.getScaledCopy(Window.getScale()),coordinates[0], coordinates[1]);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void drawUnder(Graphics g) {}
    public void drawOver(Graphics g) {}

    protected abstract void drawBuffered(Graphics b);


}
