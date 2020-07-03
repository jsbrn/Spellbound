package events.event;

import assets.definitions.DialogueDefinition;
import events.Event;

public class PlayerReplyEvent extends Event {

    private Integer player;
    private Integer npc;
    private DialogueDefinition dialogue;
    private int option;

    public PlayerReplyEvent(Integer npc, Integer player, DialogueDefinition dialogue, int option) {
        this.npc = npc;
        this.player = player;
        this.dialogue = dialogue;
        this.option = option;
    }

    public Integer getNPC() {
        return npc;
    }
    public Integer getPlayer() { return player; }

    public DialogueDefinition getDialogue() {
        return dialogue;
    }

    public int getOption() {
        return option;
    }
}
