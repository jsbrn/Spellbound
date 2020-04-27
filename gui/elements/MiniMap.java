package gui.elements;

import assets.Assets;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.Chunk;
import world.Region;
import world.World;

public class MiniMap extends GUIElement {

    private Region region;
    int size;
    private Image buffer;

    public MiniMap() {
        this.size = 32;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    private Region getRegion() {
        return region == null ? World.getLocalPlayer().getLocation().getRegion() : region;
    }

    private Image getBuffer() {
        if (getRegion() == null) return null;
        if (buffer == null || buffer.getWidth() != getRegion().getSize()) {
            buffer = Assets.getCachedBuffer(getRegion().getSize(), getRegion().getSize());
        }
        return buffer;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{size + 4, size + 4};
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

    private void drawToBuffer(Image buffer) {
        Graphics map = null;
        try {
            map = buffer.getGraphics();
            map.clear();
            Region current = getRegion();
            for (int x = 0; x < current.getSize(); x++) {
                for (int y = 0; y < current.getSize(); y++) {
                    map.setColor(current.doesChunkExist(x, y) ? current.getChunkGenerator(x, y).getColor() : Color.blue);
                    map.fillRect(x , y, 1, 1);
                }
            }
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        b.drawImage(Assets.getImage("assets/gui/minimap.png"), 0, 0);
        drawToBuffer(getBuffer());
        b.drawImage(getBuffer().getScaledCopy((float)size / (float)getRegion().getSize()), 2, 2);
    }

    @Override
    public void drawOver(Graphics g) {
        super.drawOver(g);
        double[] coords = World.getLocalPlayer().getLocation().getCoordinates();
        double[] percentage = new double[]{
                coords[0] / (getRegion().getSize() * Chunk.CHUNK_SIZE),
                coords[1] / (getRegion().getSize() * Chunk.CHUNK_SIZE)
        };
        World.getLocalPlayer().draw(
                getOnscreenCoordinates()[0] + (Window.getScale()*2) + ((float)percentage[0] * size * Window.getScale()),
                getOnscreenCoordinates()[1] + (Window.getScale()*2) + ((float)percentage[1] * size * Window.getScale()), 1);
    }

}
