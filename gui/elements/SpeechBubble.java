package gui.elements;

import assets.Assets;
import assets.definitions.DialogueDefinition;
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
    private DialogueDefinition dialogue;
    private Image background;
    private TextLabel label;
    private TextLabel options;

    public SpeechBubble() {
        this.label = new TextLabel("", 4, Chunk.TILE_SIZE * 5, 4, Color.black, false);
        this.options = new TextLabel("", 3, Color.gray, false);
        this.addChild(label, Chunk.TILE_SIZE * 2, 6, GUIAnchor.TOP_LEFT);
        this.addChild(options, 0, -4, GUIAnchor.BOTTOM_MIDDLE);
        this.background = Assets.getImage("assets/gui/dialogue.png");
    }

    public void setSpeaker(Entity speaker) {
        this.speaker = speaker;
    }

    public void setDialogue(DialogueDefinition dialogue) {
        this.label.setText(dialogue.getRandomText());
        String optionsLabel = "";
        for (int o = 0; o < dialogue.getOptionCount(); o++)
            optionsLabel += "["+(o+1)+"] "+dialogue.getOptionText(o)+" ";
        this.options.setText(optionsLabel.trim());
        this.dialogue = dialogue;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{background.getWidth(), background.getHeight()};
    }

    @Override
    public boolean onMouseMoved(int ogx, int ogy) {
        return false;
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
        if (key >= 2 && key <= 9) {
            EventDispatcher.invoke(new PlayerReplyEvent(speaker, World.getLocalPlayer(), dialogue, key - 2));
        } else if (key == Input.KEY_ESCAPE) {
            EventDispatcher.invoke(new PlayerReplyEvent(speaker, World.getLocalPlayer(), dialogue, -1));
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
                (float)(getCoordinates()[1] + Chunk.TILE_SIZE) * Window.getScale(),
                Window.getScale(), 3);
    }
}
