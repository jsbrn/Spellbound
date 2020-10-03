package gui.elements;

import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Input;
import gui.GUIAnchor;
import gui.GUIElement;
import misc.Window;

public class TextBox extends GUIElement {

    private int[] dims;
    private String text, regexKeep;
    private boolean focused;
    private TextLabel label;

    public TextBox(int w, int h) {
        this(w, h, ".*");
    }

    public TextBox(int w, int h, String regexKeep) {
        this.regexKeep = regexKeep;
        this.dims = new int[]{w, h};
        this.text = "";
        this.focused = false;
        this.label = new TextLabel("", h-3, w-2, 1, Color.black, Color.black, false, false);
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
    public boolean onKeyDown(int key, char c) {
        System.out.println(c);
        if ((c < 32 || c > 136) && key != Input.KEY_BACK) return false;
        if (!(c+"").matches(regexKeep)) return false;
        if (!focused) return false;

        getGUI().getParent().getInput().enableKeyRepeat();
        boolean control = getGUI().getParent().getInput().isKeyDown(Input.KEY_LCONTROL)
                || getGUI().getParent().getInput().isKeyDown(Input.KEY_RCONTROL);

        if (key == Input.KEY_BACK) {
            text = control ? "" : text.substring(0, Math.max(0, text.length() - 1));
        } else {
            text += c;
        }

        label.setText(text);
        return true;
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