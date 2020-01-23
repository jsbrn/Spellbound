package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.Chunk;
import world.entities.Entity;

public class SpeechBubble extends GUIElement {

    private Entity speaker;
    private String text;
    private Image background;

    public SpeechBubble(Entity speaker, String text) {
        this.speaker = speaker;
        this.text = "\""+text+"\"";
        this.addChild(new Label(this.text, 4, Chunk.TILE_SIZE * 4, 4, Color.black), 0, 0, GUIAnchor.CENTER);
        try {
            this.background = new Image("assets/gui/dialogue.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int[] getDimensions() {
        return new int[]{background.getWidth(), background.getHeight()};
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
    public boolean onKeyDown(int key) {
        return false;
    }

    @Override
    public boolean onKeyUp(int key) {
        return false;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        b.drawImage(background, 0, 0);
    }

    @Override
    public void drawOver(Graphics g) {
        super.drawOver(g);
        speaker.draw(
                (float)(getCoordinates()[0] + Chunk.TILE_SIZE) * Window.getScale(),
                (float)(getCoordinates()[1] + (getDimensions()[1]/2) + Chunk.TILE_SIZE/2) * Window.getScale(),
                Window.getScale());
    }
}
