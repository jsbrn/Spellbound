package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;

public class Picker extends GUIElement {

    private int value, minWidth;

    private Button left, right;
    private TextLabel label;

    private String[] labels;

    public Picker(int min, int max, int interval, int minWidth, String[] labels) {
        this.labels = labels;
        this.minWidth = minWidth;
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
        this.label = new TextLabel(labels != null && labels.length > 0 ? labels[value] : "", 5, Color.white, true, false);
        addChild(label, 0, 0, GUIAnchor.CENTER);
        addChild(left, 0, 0, GUIAnchor.LEFT_MIDDLE);
        addChild(right, 0, 0, GUIAnchor.RIGHT_MIDDLE);
        refresh();
    }

    public void onValueChange() {}

    private void refresh() {
        if (labels != null) label.setText(labels[value]);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (this.value != value) {
            refresh();
            onValueChange();
        }
        this.value = value;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{Math.max(label.getDimensions()[0], minWidth) + 20, left.getDimensions()[1]};
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
