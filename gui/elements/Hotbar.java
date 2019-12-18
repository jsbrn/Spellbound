package gui.elements;

import gui.GUIElement;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Hotbar extends GUIElement {

    private Image image;

    public Hotbar() {
        try {
            this.image = new Image("assets/gui/hotbar.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{ image.getWidth(), image.getHeight() };
    }

    @Override
    public boolean onClick(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onKeyPress(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b) {
        b.drawImage(image, 0, 0);
    }

}
