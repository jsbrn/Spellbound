package gui.elements;

import assets.Assets;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.lang.annotation.Inherited;

public class Label extends GUIElement {

    private String text;
    private Color color;
    private float font_size;

    public Label(String text, int height, Color color) {
        this.text = text;
        this.color = color;
        this.font_size = height;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{
                (int)(Assets.getFont(font_size * Window.getScale()).getWidth(text) / Window.getScale()),
                (int)font_size
        };
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return false;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) { return false; }

    @Override
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    public void drawOver(Graphics g) {
        float[] coords = getOnscreenCoordinates();
        g.setFont(Assets.getFont(font_size * Window.getScale()));
        g.setColor(color);
        g.drawString(text, coords[0], coords[1]);
    }

    @Override
    protected void drawBuffered(Graphics b) {
        return;
    }

}
