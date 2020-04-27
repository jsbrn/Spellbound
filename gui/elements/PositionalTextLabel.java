package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Camera;
import world.Chunk;

public class PositionalTextLabel extends GUIElement {

    private double direction, speed, lifespan, elapsedTime, offset;
    private Location location;

    public PositionalTextLabel(Location location, String text, Color color, double direction, double speed, double lifespan, double offset) {
        this(location, text, color);
        this.direction = direction;
        this.speed = speed;
        this.lifespan = lifespan;
        this.offset = offset;
    }

    private PositionalTextLabel(Location location, String text, Color color) {
        this.location = location;
        addChild(new TextLabel(text, 4, color, true), 0, 0, GUIAnchor.CENTER);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{0, 0};
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

    @Override
    public void drawOver(Graphics g) {
        elapsedTime += MiscMath.getConstant(1000, 1);
        double[] rotatedOffset = MiscMath.getRotatedOffset(0, -MiscMath.getConstant(speed * Chunk.TILE_SIZE, 1) * (elapsedTime / 1000), direction);
        float[] osc = Camera.getOnscreenCoordinates(location.getCoordinates()[0] + rotatedOffset[0], location.getCoordinates()[1] + rotatedOffset[1] + offset, Window.getScale());
        this.setOffset(osc[0]/Window.getScale(), osc[1]/Window.getScale());
        if (elapsedTime >= lifespan) getGUI().removeElement(this);
    }
}
