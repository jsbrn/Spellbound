package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class Button extends GUIElement {

    private int[] dims;
    private Color color, highlightColor, disabledColor;
    private boolean toggled, showBackground, disabled;

    private IconLabel iconLabel;

    public Button(String text, int w, int h, String icon, boolean showBackground) {
        if (text != null)
            this.addChild(new TextLabel(text, 3, w, 1, Color.white, true), 0, -1, GUIAnchor.CENTER);
        this.showBackground = showBackground;
        this.dims = new int[]{w, h};
        this.color = new Color(170, 115, 65);
        this.highlightColor = new Color(105, 196, 235);
        this.disabledColor = new Color(100, 100, 100);
        this.iconLabel = icon != null ? new IconLabel(icon) : new IconLabel();
        this.addChild(iconLabel, 0, 0, GUIAnchor.CENTER);
    }

    public void setIcon(Image image) {
        this.iconLabel.setImage(image);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setIconFilter(Color filter) {
        iconLabel.setFilter(filter);
    }

    public Color getColor() {
        return color;
    }

    public void setHighlightColor(Color color) {
        this.highlightColor = color;
    }

    @Override
    public int[] getDimensions() { return dims; }

    @Override
    public final boolean onMouseRelease(int ogx, int ogy, int button) { return false; }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public final boolean onMousePressed(int ogx, int ogy, int button) {
        if (mouseIntersects() && !disabled) {
            onClick(button);
            return true;
        }
        return false;
    }

    public void setEnabled(boolean e) { this.disabled = !e; }

    public boolean isToggled() { return toggled; }
    public void setToggled(boolean t) { toggled = t; }

    public abstract boolean onClick(int button);

    @Override
    public boolean onMouseMoved(int ogx, int ogy) { return false; }

    @Override
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        if (!showBackground) return;
        Color c = disabled ? disabledColor : (mouseHovering || toggled ? highlightColor : color);
        Color darker = c.darker(), lighter = c.brighter();
        for (int i = 0; i < dims[0]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                b.setColor(c);
                if (i == dims[0] - 1 || j == dims[1] - 1) b.setColor(!(mouseDown && mouseHovering) && !toggled ? darker : lighter);
                if (i == 0 || j == 0) b.setColor(!(mouseDown && mouseHovering) && !toggled ? lighter : darker);
                b.fillRect(i, j, 1, 1);
            }
        }
    }
}
