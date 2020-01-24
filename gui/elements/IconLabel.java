package gui.elements;

import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class IconLabel extends GUIElement {

    private int density, size;
    private Image image;

    public IconLabel(String image, int size) {
        this.size = size;
        try {
            this.image = new Image("assets/gui/icons/"+image, false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{size, size};
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
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {

    }

    private int[] getScaledSize() {
        float scale = ((float)size / (float)image.getHeight()) * Window.getScale();
        return new int[]{(int)(image.getWidth() * scale), (int)(image.getHeight() * scale)};
    }

    @Override
    public void drawOver(Graphics g) {
        float[] osc = getOnscreenCoordinates();
        g.drawImage(image.getScaledCopy(getScaledSize()[0], getScaledSize()[1]), osc[0], osc[1]);
    }
}
