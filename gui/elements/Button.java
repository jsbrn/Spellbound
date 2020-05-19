package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import gui.sound.SoundManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public abstract class Button extends GUIElement {

    private int[] dims;
    private Color color, toggledColor, disabledColor;
    private boolean toggled, showBackground, disabled;

    private IconLabel iconLabel;

    public Button(String text, int w, int h, String icon, boolean showBackground) {
        if (text != null)
            this.addChild(new TextLabel(text, 3, w, 1, Color.white, true, false), 0, -1, GUIAnchor.CENTER);
        this.showBackground = showBackground;
        this.dims = new int[]{w, h};
        this.color = new Color(170, 115, 65);
        this.toggledColor = new Color(105, 196, 235);
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

    private Color getCurrentColor() {
        return toggled ? toggledColor : (disabled ? disabledColor : color);
    }

    @Override
    public int[] getDimensions() { return dims; }

    @Override
    public final boolean onMouseRelease(int ogx, int ogy, int button) {
        if (mouseIntersects() && !disabled) {
            SoundManager.playSound(SoundManager.CLICK);
            onClick(button);
            return true;
        }
        return false;
    }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public final boolean onMousePressed(int ogx, int ogy, int button) {
        return false;
    }

    public void setEnabled(boolean e) { this.disabled = !e; }

    public boolean isToggled() { return toggled; }
    public void setToggled(boolean t) { toggled = t; }

    public abstract void onClick(int button);

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
        boolean pushedIn = (mouseDown && mouseIntersects()) || toggled;
        Color c = toggled ? toggledColor : (mouseIntersects() ? getCurrentColor().brighter(0.3f) : getCurrentColor());
        Color darkBorder = pushedIn ? c.brighter() : c.darker(), lightBorder = pushedIn ? c.darker() : c.brighter();
        for (int i = 0; i < dims[0]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                b.setColor(c);
                if (i == dims[0] - 1 || j == dims[1] - 1) b.setColor(darkBorder);
                if (i == 0 || j == 0) b.setColor(lightBorder);
                b.fillRect(i, j, 1, 1);
            }
        }
    }
}
