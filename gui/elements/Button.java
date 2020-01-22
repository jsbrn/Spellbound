package gui.elements;

import gui.GUIElement;
import gui.states.GameScreen;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Button extends GUIElement {

    private Image image;
    private int[] dims;
    private Color color, highlightColor;
    private String text;

    public Button(int w, int h) {
        this.dims = new int[]{w, h};
        this.color = new Color(170, 115, 65);
    }

    public Button(String image) {
        try {
            this.image = new Image("assets/gui/"+image, false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() { return image == null ? dims : new int[]{ image.getWidth(), image.getHeight() }; }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) { return false; }

    @Override
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b) {
        if (image != null) {
            b.drawImage(image, 0, 0);
        } else {
            for (int i = 0; i < dims[0]; i++) {
                for (int j = 0; j < dims[1]; j++) {
                    b.setColor(color);
                    if (i == dims[0] - 1 || j == dims[1] - 1) b.setColor(color.darker());
                    if (i == 0 || j == 0) b.setColor(color.brighter());
                    b.fillRect(i, j, 1, 1);
                }
            }
        }
    }
}
