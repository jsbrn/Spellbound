package gui.elements;

import assets.Assets;
import gui.GUIAnchor;
import gui.GUIElement;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Picker extends GUIElement {

    private int value;

    private Button left, right;
    private TextLabel label;

    private String[] labels;

    public Picker(int min, int max, int interval, String labels[]) {
        this.left = new Button(null, 8, 8, "icons/arrow_left.png", true) {
            @Override
            public void onClick(int button) {
                value -= interval;
                if (value < min) value = max;
                onValueChange();
                refresh();
            }
        };
        this.right = new Button(null, 8, 8, "icons/arrow_right.png", true) {
            @Override
            public void onClick(int button) {
                value += interval;
                if (value > max) value = min;
                onValueChange();
                refresh();
            }
        };
        this.label = new TextLabel("", 5, Color.white, true, false);
        addChild(label, 0, 0, GUIAnchor.CENTER);
        addChild(left, 0, 0, GUIAnchor.LEFT_MIDDLE);
        addChild(right, 0, 0, GUIAnchor.RIGHT_MIDDLE);
        refresh();
    }

    public void onValueChange() {}

    public void refresh() {
        if (labels != null) label.setText(labels[value]);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (this.value != value) onValueChange();
        this.value = value;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{Math.max(label.getDimensions()[0], 8) + 20, left.getDimensions()[1]};
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
