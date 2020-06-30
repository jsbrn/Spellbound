package world.events.event;

import world.events.Event;

public class ConversationEndedEvent extends Event {

    private Integer npc, player;

    public ConversationEndedEvent(Integer npc, Integer player) {
        this.npc = npc;
        this.player = player;
    }

    public Integer getNPC() {
        return npc;
    }

    public Integer getPlayer() {
        return player;
    }

}
