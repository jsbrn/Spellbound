package world.events.event;

import assets.definitions.DialogueDefinition;
import world.entities.types.humanoids.Player;
import world.events.Event;

public class NPCSpeakEvent extends Event {

    private Entity npc;
    private Player player;

    private DialogueDefinition dialogue;
    private String[] options;

    public NPCSpeakEvent(Entity npc, Player player, DialogueDefinition dialogue) {
        this.npc = npc;
        this.player = player;
        this.dialogue = dialogue;
    }

    public Entity getNPC() {
        return npc;
    }
    public Entity getPlayer() {
        return player;
    }
    public DialogueDefinition getDialogue() { return dialogue; }
}
