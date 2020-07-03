package events.event;

import events.Event;

public class ConversationStartedEvent extends Event {

    private Integer npc, player;

    public ConversationStartedEvent(Integer npc, Integer player) {
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
