package world.events.event;

import assets.definitions.DialogueDefinition;
import world.entities.Entity;
import world.entities.types.humanoids.Player;
import world.events.Event;

public class PlayerReplyEvent extends Event {

    private Player player;
    private Entity npc;
    private DialogueDefinition dialogue;
    private int option;

    public PlayerReplyEvent(Entity npc, Player player, DialogueDefinition dialogue, int option) {
        this.npc = npc;
        this.player = player;
        this.dialogue = dialogue;
        this.option = option;
    }

    public Entity getNPC() {
        return npc;
    }
    public Player getPlayer() { return player; }

    public DialogueDefinition getDialogue() {
        return dialogue;
    }

    public int getOption() {
        return option;
    }
}
