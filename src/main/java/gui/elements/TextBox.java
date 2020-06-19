package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import misc.Window;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Input;

public class TextBox extends GUIElement {

    private int[] dims;
    private String text;
    private boolean focused;
    private TextLabel label;

    public TextBox(int w, int h) {
        this.dims = new int[]{w, h};
        this.text = "";
        this.focused = false;
        this.label = new TextLabel("", h-3, w-2, 1, Color.black, false, false);
        addChild(label, 1, 1, GUIAnchor.TOP_LEFT);
    }

    public void setText(String text) {
        this.text = text;
        label.setText(text);
    }

    public String getText() {
        return text;
    }

    @Override
    public int[] getDimensions() {
        return dims;
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
        focused = mouseIntersects();
        return focused;
    }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public boolean onKeyDown(int key) {
        if (focused) {
            getGUI().getParent().getInput().enableKeyRepeat();
            boolean shifting = getGUI().getParent().getInput().isKeyDown(Input.KEY_LSHIFT)
                || getGUI().getParent().getInput().isKeyDown(Input.KEY_RSHIFT);
            boolean control = getGUI().getParent().getInput().isKeyDown(Input.KEY_LCONTROL)
                    || getGUI().getParent().getInput().isKeyDown(Input.KEY_RCONTROL);
            if (key == Input.KEY_BACK && !text.isEmpty()) {
                if (control)
                    text = "";
                else
                    text = text.substring(0, text.length() - 1);
            }
            if (key == Input.KEY_SPACE) text += " ";
            String charr = shifting ? Input.getKeyName(key).toUpperCase() : Input.getKeyName(key).toLowerCase();
            if (charr.matches("[A-Z]|[a-z]|[0-9]")) text += charr;
            label.setText(text);
            return true;
        }
        return false;
    }

    public void grabFocus() { this.focused = true; }

    public void releaseFocus() {
        this.focused = false;
    }

    @Override
    public boolean onKeyUp(int key) {
        getGUI().getParent().getInput().disableKeyRepeat();
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        for (int i = 0; i < dims[0]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                if (i == dims[0] - 1 || j == dims[1] - 1 || i == 0 || j == 0)
                    b.setColor(!focused ? Color.black : Color.darkGray);
                else
                    b.setColor(Color.white);
                b.fillRect(i, j, 1, 1);
            }
        }
    }

    @Override
    public void drawOver(Graphics g) {
        if (!focused) return;
        if ((System.currentTimeMillis() / 500) % 2 == 0) return;
        float[] osc = label.getOnscreenCoordinates();
        osc[0] += label.getDimensions()[0] * Window.getScale();
        g.setColor(Color.black);
        g.fillRect(osc[0] + (Window.getScale()/2), osc[1] + (Window.getScale()/4), Window.getScale() / 2, label.getDimensions()[1] * Window.getScale());
    }
}