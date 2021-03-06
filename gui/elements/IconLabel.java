package gui.elements;

import assets.Assets;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class IconLabel extends GUIElement {

    private Image image;
    private Color filter;

    public IconLabel() {
        this.filter = Color.white;
    }

    public IconLabel(String image) {
        this();
        this.image = Assets.getImage("assets/gui/" + image);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setFilter(Color filter) {
        this.filter = filter;
    }

    @Override
    public int[] getDimensions() {
        if (image == null) return new int[]{1, 1};
        return new int[]{image.getWidth(), image.getHeight()};
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return false;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) {
        return false;
    }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

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
        if (image == null) return;
        b.clear();
        b.drawImage(image, 0, 0, filter);
    }
}
