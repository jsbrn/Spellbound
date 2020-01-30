package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Canvas extends GUIElement {

    private int[] dims;
    private boolean[][] grid;
    private Color base, under;
    private int scale;

    private boolean draw, held;

    public Canvas(int w, int h, int scale) {
        this.base = Color.red;
        this.under = new Color(170, 115, 65, 255/2);
        this.scale = scale;
        this.grid = new boolean[w][h];
        this.dims = new int[]{(int)(w * scale), (int)(h * scale)};
        Color[] colors = new Color[]{
                Color.red,
                Color.orange,
                Color.yellow,
                Color.green,
                Color.blue,
                Color.pink.darker(),
                Color.black.brighter(0.25f),
                Color.white
        };
        for (int i = 0; i < w/2; i++) {
            Button color = new Button(null, scale, scale, null, true) {
                @Override
                public boolean onClick(int button) {
                    base = getColor().darker(0.1f);
                    return true;
                }
            };
            System.out.println(i);
            color.setColor(colors[i]);
            color.setHighlightColor(colors[i].brighter());
            addChild(color, i * scale, -scale, GUIAnchor.TOP_LEFT);
        }

        Button clearButton = new Button("X", scale, scale, null, false) {
            @Override
            public boolean onClick(int button) {
                grid = new boolean[w][h];
                return false;
            }
        };
        addChild(clearButton, -scale, -scale, GUIAnchor.TOP_RIGHT);

    }

    public void reset() {
        this.grid = new boolean[dims[0]/scale][dims[1]/scale];
        this.base = Color.white;
    }

    public boolean setSell(int x, int y, boolean val) {
        if (x < 0 || x >= grid[0].length || y < 0 || y >= grid[0].length) return false;
        grid[x][y] = val;
        return true;
    }

    public boolean getCell(int x, int y) {
        if (x < 0 || x >= grid[0].length || y < 0 || y >= grid[0].length) return false;
        return grid[x][y];
    }

    public boolean[][] getGrid() { return grid; }

    public Color getColor() {
        return base;
    }

    @Override
    public int[] getDimensions() {
        return dims;
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return held && setSell((int)(ogx / scale), (int)(ogy / scale), draw);
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        held = false;
        return false;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) {
        held = true;
        draw = !getCell((int)(ogx / scale), (int)(ogy / scale));
        return setSell((int)(ogx / scale), (int)(ogy / scale), draw);
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
        b.clear();
        b.setColor(base);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]) b.fillRect(i * scale, j * scale, scale, scale);
            }
        }
    }

    @Override
    public void drawUnder(Graphics g) {
        float[] osc = getOnscreenCoordinates();
        g.setColor(under);
        g.fillRect(osc[0], osc[1], getDimensions()[0] * Window.getScale(), getDimensions()[1] * Window.getScale());
    }

}
