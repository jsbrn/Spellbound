package world.events.event;

import assets.definitions.DialogueDefinition;
import world.events.Event;

public class NPCSpeakEvent extends Event {

    private int npc, player;

    private DialogueDefinition dialogue;
    private String[] options;

    public NPCSpeakEvent(Integer npc, Integer player, DialogueDefinition dialogue) {
        this.npc = npc;
        this.player = player;
        this.dialogue = dialogue;
    }

    public Integer getNPC() {
        return npc;
    }
    public Integer getPlayer() {
        return player;
    }
    public DialogueDefinition getDialogue() { return dialogue; }
}
