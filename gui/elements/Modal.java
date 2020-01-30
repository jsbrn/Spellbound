package gui.elements;

import gui.GUIElement;
import org.newdawn.slick.*;
import world.World;

public class Modal extends GUIElement {

    private Image image;

    public Modal(String image) {
        try {
            this.image = new Image("assets/gui/"+image, false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{image.getWidth(), image.getHeight()};
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return false;
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return true;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) {
        return true;
    }

    @Override
    public void drawUnder(Graphics g) {

    }

    @Override
    public void drawOver(Graphics g) {

    }

    @Override
    public boolean onKeyUp(int key) {
        return true;
    }

    @Override
    public boolean onKeyDown(int key) {
        if (key == Input.KEY_TAB || key == Input.KEY_ESCAPE) {
            getGUI().popModal();
        }
        return true;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        b.drawImage(image, 0, 0);
    }

}
