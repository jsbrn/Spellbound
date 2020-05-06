package gui.elements;

import assets.Assets;
import gui.GUIAnchor;
import gui.GUIElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class IconChooser extends GUIElement {

    private String folder;
    private int index, scale;

    private Image image;
    private Color color;

    private Button left, right;
    private IconLabel iconLabel;

    public IconChooser(String folder, int iconCount, int scale) {
        this.scale = scale;
        this.folder = folder;
        this.left = new Button(null, 8, 8, "icons/arrow_left.png", true) {
            @Override
            public void onClick(int button) {
                index = index == 0 ? iconCount - 1 : index - 1;
                refresh();
            }
        };
        this.right = new Button(null, 8, 8, "icons/arrow_right.png", true) {
            @Override
            public void onClick(int button) {
                index = index == iconCount - 1 ? 0 : index + 1;
                refresh();
            }
        };
        this.iconLabel = new IconLabel();
        addChild(iconLabel, 0, 0, GUIAnchor.TOP_LEFT);
        addChild(left, 0, 0, GUIAnchor.BOTTOM_LEFT);
        addChild(right, 0, 0, GUIAnchor.BOTTOM_RIGHT);
        refresh();
    }

    public void refresh() {
        image = Assets.getImage(folder+"/"+index+".png").getScaledCopy(scale);
        this.iconLabel.setImage(image);
        this.iconLabel.setFilter(color);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setColor(Color color) {
        this.color = color;
        refresh();
    }

    @Override
    public int[] getDimensions() {
        return new int[]{image.getWidth(), image.getHeight() + 2 + left.getDimensions()[1]};
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

    }

}
