package world.events.event;

import world.entities.Entity;
import world.entities.types.humanoids.Player;
import world.events.Event;

public class PlayerReplyEvent extends Event {

    private Player player;
    private Entity npc;
    private int option;

    public PlayerReplyEvent(Entity npc, Player player, int option) {
        this.npc = npc;
        this.player = player;
        this.option = option;
    }

    public Entity getNPC() {
        return npc;
    }
    public Player getPlayer() {
        return player;
    }

    public int getOption() {
        return option;
    }
}
