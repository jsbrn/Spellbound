package gui.elements;

import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Backdrop extends GUIElement {

    private Image image;

    public Backdrop() {
        try {
            this.image = new Image("assets/gui/wood.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{ Window.getWidth(), Window.getHeight() };
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
        int[] dimensions = getDimensions();
        for (int i = 0; i < dimensions[0] / image.getWidth(); i++)
            for (int j = 0; j < dimensions[1] / image.getHeight(); j++)
                b.drawImage(image, i * image.getWidth(), j * image.getHeight());
    }

}
