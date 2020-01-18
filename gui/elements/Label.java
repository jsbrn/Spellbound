package gui.elements;

import assets.Assets;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.lang.annotation.Inherited;

public class Label extends GUIElement {

    private String text;
    private int font_size;
    private Color color;

    public Label(String text, int font_size, Color color) {
        this.text = text;
        this.font_size = font_size;
        this.color = color;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{
                (int)(Assets.FONT.getWidth(text) / Window.getScale()),
                (int)(Assets.FONT.getHeight() / Window.getScale())
        };
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy) { return false; }

    @Override
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    public void draw(Graphics g) {
        int[] coords = getCoordinates();
        coords[0] *= Window.getScale();
        coords[1] *= Window.getScale();
        g.setFont(Assets.FONT);
        g.setColor(color);
        g.drawString(text, coords[0], coords[1]);
    }

    @Override
    protected void drawBuffered(Graphics b) {
        return;
    }

}
