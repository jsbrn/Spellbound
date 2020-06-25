package world.events.event;

import world.events.Event;

public class ConversationStartedEvent extends Event {

    private Entity npc, player;

    public ConversationStartedEvent(Entity npc, Entity player) {
        this.npc = npc;
        this.player = player;
    }

    public Entity getNPC() {
        return npc;
    }

    public Entity getPlayer() {
        return player;
    }

}
