package gui.elements;

import gui.GUIElement;
import gui.states.GameScreen;
import misc.Window;
import org.newdawn.slick.*;

public class Modal extends GUIElement {

    private Color background = new Color(0, 0, 0, 0.5f);
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
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return true;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) {
        return true;
    }

    @Override
    public boolean onKeyDown(int key) {
        return true;
    }

    @Override
    public boolean isActive() {
        return getGUI().isModal(this);
    }

    @Override
    public boolean onKeyUp(int key) {
        if (key == Input.KEY_TAB) {
            getGUI().setModal(getGUI().isModal(this) ? null : this);
            hide();
            GameScreen.setPaused(false);
        }
        return true;
    }

    @Override
    public void drawUnder(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, Window.getWidth(), Window.getHeight());
    }

    @Override
    protected void drawBuffered(Graphics b) {
        b.drawImage(image, 0, 0);
    }

}
