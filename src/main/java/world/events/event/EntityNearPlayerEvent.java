package world.events.event;

import world.events.Event;

public class EntityNearPlayerEvent extends Event {

    private int playerID, entityID;

    public EntityNearPlayerEvent(int entityID, int playerID) {
        this.playerID = playerID;
        this.entityID = entityID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getEntityID() {
        return entityID;
    }
}
