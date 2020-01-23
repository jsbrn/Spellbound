package world.events.event;

import world.entities.Entity;
import world.entities.types.humanoids.Player;
import world.events.Event;

public class NPCSpeakEvent extends Event {

    private Entity npc;
    private Player player;

    private String message;
    private String[] options;

    public NPCSpeakEvent(Entity npc, Player player, String text, String[] options) {
        this.npc = npc;
        this.player = player;
        this.message = text;
        this.options = options;
    }

    public Entity getNPC() {
        return npc;
    }
    public Entity getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public String[] getOptions() {
        return options;
    }
}
