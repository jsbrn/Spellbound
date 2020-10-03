package gui.elements;

import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import gui.GUIElement;
import misc.MiscMath;

public class ColorChooser extends GUIElement {

    private int rows, cols, scale;
    private int[] clicked;

    public ColorChooser(int rows, int cols, int scale) {
        this.rows = rows;
        this.cols = cols;
        this.scale = scale;
        this.clicked = new int[]{0, 0};
    }

    @Override
    public int[] getDimensions() {
        return new int[]{cols * scale, rows * scale};
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
        if (!mouseIntersects()) return false;
        int r = ogy / scale;
        int c = ogx / scale;
        clicked[0] = r; clicked[1] = c;
        return true;
    }

    @Override
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public boolean onKeyDown(int key, char c) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    public void setColor(Color c) {
        float[] hsb = java.awt.Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), new float[3]);
        int col = (int)MiscMath.round(hsb[1] * cols, 1 / (float)cols);
        int row = (int)MiscMath.round(col == 0 ? (1-hsb[2]) * rows : hsb[0] * rows, 1 / (float)rows);
        clicked[0] = row; clicked[1] = col;
    }

    public Color getColor() {
        return getColor(clicked[0], clicked[1]);
    }

    public Color getColor(int row, int column) {
        float hue = (float)row/(float)rows,
                saturation = (float)column/(float)cols,
                brightness = column > 0 ? 1f : 1 - ((float)row/(float)rows);
        java.awt.Color myRGBColor = java.awt.Color.getHSBColor(hue, saturation, brightness);
        Color from = new Color(myRGBColor.getRed(), myRGBColor.getGreen(), myRGBColor.getBlue());
        return from;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                b.setColor(getColor(r, c));
                b.fillRect(c * scale, r * scale, scale, scale);
                if (r == clicked[0] && c == clicked[1]) {
                    b.setColor(Color.black);
                    b.drawRect(c * scale, r * scale, scale - 1, scale - 1);
                }
            }
        }
    }

}
