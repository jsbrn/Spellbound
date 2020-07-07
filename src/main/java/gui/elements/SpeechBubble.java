package gui.elements;

import assets.Assets;
import assets.definitions.DialogueDefinition;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import com.github.mathiewz.slick.Image;
import com.github.mathiewz.slick.Input;
import gui.GUIAnchor;
import gui.GUIElement;
import gui.sound.SoundManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import world.Chunk;
import world.World;
import world.events.EventManager;
import world.events.event.PlayerReplyEvent;

import java.util.ArrayList;

public class SpeechBubble extends GUIElement {

    private int speaker;
    private DialogueDefinition dialogue;
    private Image background;
    private TextLabel contents;
    private TextLabel title;
    private ArrayList<Button> options;

    public SpeechBubble() {
        this.title = new TextLabel("", 6, Chunk.TILE_SIZE * 5, 1, Color.white, Color.white, true, false);
        this.contents = new TextLabel("", 4, (Chunk.TILE_SIZE*11)/2, 6, Color.black, Color.white, false, false);
        this.options = new ArrayList<>();
        this.addChild(title, 4, -8, GUIAnchor.TOP_LEFT);
        this.addChild(contents, Chunk.TILE_SIZE * 2, 5, GUIAnchor.TOP_LEFT);
        this.background = Assets.getImage("gui/dialogue.png");
    }

    public void setSpeaker(int speaker) {
        this.speaker = speaker;
        this.title.setText("(not implemented)");
        SoundManager.playSound(SoundManager.PAGE_TURN);
    }

    public void setDialogue(DialogueDefinition dialogue) {
        this.contents.setText(dialogue.getRandomText());
        String optionsLabel = "";
        this.options.forEach(b -> removeChild(b));
        this.options.clear();
        for (int o = 0; o < dialogue.getOptionCount(); o++) {
            String text = dialogue.getOptionText(o);
            int optionId = o;
            Button option = new Button(dialogue.getOptionText(optionId), getDimensions()[0], 6, null, true) {
                @Override
                public void onClick(int button) {
                    throw new NotImplementedException();
                }
            };
            addChild(option, 0, -(dialogue.getOptionCount() - 1 - o) * 7, GUIAnchor.BOTTOM_MIDDLE);
            options.add(option);
        }
        this.dialogue = dialogue;
    }

    @Override
    public int[] getDimensions() {
        return new int[]{background.getWidth(), background.getHeight() + (options.size() * 7)};
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
    public boolean onMouseScroll(int direction) {
        return false;
    }

    @Override
    public boolean onKeyDown(int key) {
        return true;
    }

    @Override
    public boolean onKeyUp(int key) {
        throw new NotImplementedException();
    }

    @Override
    protected void drawBuffered(Graphics b, boolean mouseHovering, boolean mouseDown) {
        b.drawImage(background, 0, 0);
    }

    @Override
    public void drawOver(Graphics g) {
        super.drawOver(g);
//        if (speaker == null) return;
//        speaker.draw(
//                (float)(getCoordinates()[0] + Chunk.TILE_SIZE) * Window.getScale(),
//                (float)(getCoordinates()[1] + Chunk.TILE_SIZE) * Window.getScale(),
//                Window.getScale(), 3);
    }
}
