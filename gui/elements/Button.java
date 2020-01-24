package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class Button extends GUIElement {

    private Image image;
    private int[] dims;
    private Color color, highlightColor;

    public Button(String text, int w, int h) {
        this.addChild(new TextLabel(text, 3, w, 1, Color.white, true), 0, -1, GUIAnchor.CENTER);
        this.dims = new int[]{w, h};
        this.color = new Color(170, 115, 65);
        this.highlightColor = new Color(105, 196, 235);
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
    public final boolean onMouseRelease(int ogx, int ogy, int button) { return false; }

    @Override
    public final boolean onMousePressed(int ogx, int ogy, int button) {
        if (mouseIntersects()) {
            onClick(button);
            return true;
        }
        return false;
    }

    public abstract boolean onClick(int button);

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
        if (image != null) {
            b.drawImage(image, 0, 0);
        } else {
            Color c = mouseHovering ? highlightColor : color;
            Color darker = c.darker(), lighter = c.brighter();
            for (int i = 0; i < dims[0]; i++) {
                for (int j = 0; j < dims[1]; j++) {
                    b.setColor(c);
                    if (i == dims[0] - 1 || j == dims[1] - 1) b.setColor(!(mouseDown && mouseHovering) ? darker : lighter);
                    if (i == 0 || j == 0) b.setColor(!(mouseDown && mouseHovering) ? lighter : darker);
                    b.fillRect(i, j, 1, 1);
                }
            }
        }
    }
}
