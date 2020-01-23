package gui.elements;

import gui.GUIAnchor;
import gui.GUIElement;
import misc.Window;
import org.newdawn.slick.*;
import world.Chunk;
import world.World;
import world.entities.Entity;
import world.events.EventDispatcher;
import world.events.event.PlayerReplyEvent;

public class SpeechBubble extends GUIElement {

    private Entity speaker;
    private String text;
    private Image background;
    private Label label;

    public SpeechBubble(Entity speaker, String text) {
        this.speaker = speaker;
        this.text = "\""+text+"\"";
        this.label = new Label(this.text, 4, Chunk.TILE_SIZE * 5, 4, Color.black);
        this.addChild(label, 0, 0, GUIAnchor.CENTER);
        this.addChild(new Label("[ENTER] Continue", 3, Color.gray), 0, -6, GUIAnchor.BOTTOM_MIDDLE);
        try {
            this.background = new Image("assets/gui/dialogue.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void setSpeaker(Entity speaker) {
        this.speaker = speaker;
    }

    public void setText(String text) {
        this.label.setText(text);
    }

    @Override
    public int[] getDimensions() {
        return new int[]{background.getWidth(), background.getHeight()};
    }

    @Override
    public boolean onMouseRelease(int ogx, int ogy, int button) {
        return true;
    }

    @Override
    public boolean onMousePressed(int ogx, int ogy, int button) {
        return true;
    }

    @Override
    public boolean onKeyDown(int key) {
        return true;
    }

    @Override
    public boolean onKeyUp(int key) {
        if (key == Input.KEY_ENTER) {
            EventDispatcher.invoke(new PlayerReplyEvent(speaker, World.getLocalPlayer(), 0));
        }
        if (key == Input.KEY_ESCAPE) {
            EventDispatcher.invoke(new PlayerReplyEvent(speaker, World.getLocalPlayer(), -1));
        }
        return true;
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        b.drawImage(background, 0, 0);
    }

    @Override
    public void drawOver(Graphics g) {
        super.drawOver(g);
        if (speaker == null) return;
        speaker.draw(
                (float)(getCoordinates()[0] + Chunk.TILE_SIZE) * Window.getScale(),
                (float)(getCoordinates()[1] + (getDimensions()[1]/2) + Chunk.TILE_SIZE/2) * Window.getScale(),
                Window.getScale());
    }
}
