package gui.elements;

import assets.Assets;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import java.util.ArrayList;

public class TextLabel extends GUIElement {

    private String text;
    private Color color;
    private boolean shadow;
    private int maxWidth, maxLines;
    private float lineHeight;
    private ArrayList<String> lines;

    public TextLabel(String text, int lineHeight, Color color, boolean shadow) {
        this(text, lineHeight, Integer.MAX_VALUE, 16, color, shadow);
    }

    public TextLabel(String text, int lineHeight, int maxWidth, int maxLines, Color color, boolean shadow) {
        this.text = text;
        this.color = color;
        this.maxWidth = maxWidth;
        this.maxLines = maxLines;
        this.lineHeight = lineHeight;
        this.lines = getLines(text);
        this.shadow = shadow;
        this.setBuffered(false);
    }

    private ArrayList<String> getLines(String text) {
        String[] words = text.split("\\s");
        ArrayList<String> rows = new ArrayList<String>();
        rows.add("");
        int currentRowSize = 0;
        int currentRowIndex = 0;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            int size = (int)(getFont().getWidth(" "+word) / Window.getScale());
            if (currentRowSize + size < maxWidth) {
                rows.set(currentRowIndex, rows.get(currentRowIndex) + " " + word);
                currentRowSize += size;
            } else {
                i--;
                if (currentRowIndex + 1 < maxLines) {
                    currentRowIndex += 1;
                    currentRowSize = 0;
                    rows.add("");
                } else {
                    rows.set(currentRowIndex, rows.get(currentRowIndex) + "...");
                    break;
                }
            }
        }
        return rows;
    }

    public void setText(String newtext) {
        if (!newtext.equals(text)) {
            lines = getLines(newtext);
            text = newtext;
        }
    }

    private TrueTypeFont getFont() { return Assets.getFont(lineHeight * Window.getScale()); }

    @Override
    public int[] getDimensions() {
        int textWidth = (int)(getFont().getWidth(text) / Window.getScale());
        return new int[]{
                textWidth < maxWidth ? textWidth : maxWidth,
                lines.size() * (int)lineHeight
        };
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return false;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) { return false; }

    @Override
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    public void drawOver(Graphics g) {
        float[] coords = getOnscreenCoordinates();
        g.setFont(Assets.getFont(lineHeight * Window.getScale()));
        for (int i = 0; i < lines.size(); i++) {
            float x = coords[0] - Window.getScale();
            float y = coords[1] + (i * lineHeight * Window.getScale());
            if (shadow) {
                g.setColor(color.darker().darker());
                g.drawString(lines.get(i), x + 1, y + 1);
            }
            g.setColor(color);
            g.drawString(lines.get(i), x, y);
        }
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {}

}
