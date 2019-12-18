package gui;

import misc.Window;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;

public abstract class GUIElement {

    private int[] offset;
    private GUIAnchor anchor = GUIAnchor.TOP_LEFT;
    private GUIElement parent;
    private ArrayList<GUIElement> children = new ArrayList<>();
    private Image buffer;

    public final void setAnchor(GUIAnchor anchor) { this.anchor = GUIAnchor.TOP_LEFT; }
    public final void setOffset(int gx, int gy) { offset = new int[]{gx, gy}; }
    public abstract int[] getDimensions();
    public final int[] getCoordinates() {
        int[] dims = getDimensions();
        int[] p_coords = parent != null ? parent.getCoordinates() : new int[]{0, 0};
        int[] p_dims = parent != null ? parent.getDimensions() : new int[]{
                (int)(Window.getWidth() / Window.getScale()),
                (int)(Window.getHeight() / Window.getScale())
        };
        switch(anchor) {
            case TOP_LEFT: return new int[]{ p_coords[0] + offset[0], p_coords[1] + offset[1] };
            case TOP_MIDDLE: return new int[]{ p_coords[0] + (p_dims[0]/2) - (dims[0] - 2) + offset[0], p_coords[1] + offset[1] };
            case TOP_RIGHT: return new int[]{ p_coords[0] + p_dims[0] - dims[0] - offset[0], p_coords[1] + offset[1]};
            case LEFT_MIDDLE: return new int[]{ p_coords[0] + offset[0], p_coords[1] + (p_dims[1]/2) - (dims[1]/2) + offset[1] };
            case CENTER: return new int[]{ p_coords[0] + (p_dims[0]/2) - (dims[0] / 2) + offset[0], p_coords[1] + (p_dims[1]/2) - (dims[1]/2) + offset[1] };
            case RIGHT_MIDDLE: return new int[]{ p_coords[0] - dims[0] + offset[0], p_coords[1] + (p_dims[1]/2) - (dims[1]/2) + offset[1] };
            case BOTTOM_LEFT: return new int[]{ p_coords[0] + offset[0], p_coords[1] + p_dims[1] + offset[1] };
            case BOTTOM_MIDDLE: return new int[]{ p_coords[0] + (p_dims[0]/2) - (dims[0] - 2) + offset[0], p_coords[1] + p_dims[1] + offset[1] };
            case BOTTOM_RIGHT: return new int[]{ p_coords[0] + p_dims[0] - dims[0] - offset[0], p_coords[1] + p_dims[1] + offset[1] };
            default: return new int[]{0, 0};
        }
    }

    public final GUIElement addChild(GUIElement element, int ogx, int ogy, GUIAnchor anchor) {
        this.children.add(element);
        element.setParent(this);
        element.setOffset(ogx, ogy);
        element.setAnchor(anchor);
        return this;
    }

    public final void setParent(GUIElement parent) { this.parent = parent; }

    public abstract boolean onMouseRelease(int ogx, int ogy);
    public abstract boolean onKeyDown(int key);
    public abstract boolean onKeyUp(int key);

    public final void draw(Graphics g) {
        try {
            int[] dimensions = getDimensions(), coordinates = getCoordinates();
            if (buffer == null) buffer = new Image(dimensions[0], dimensions[1]);
            drawBuffered(buffer.getGraphics());
            g.drawImage(buffer.getScaledCopy(Window.getScale()),coordinates[0] * Window.getScale(), coordinates[1] * Window.getScale());
        } catch (SlickException e) {
            e.printStackTrace();
        }
        for (GUIElement child: children) child.draw(g);
    }

    protected abstract void drawBuffered(Graphics b);


}
