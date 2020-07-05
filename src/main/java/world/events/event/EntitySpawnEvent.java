package world.events.event;

import world.events.Event;

public class EntitySpawnEvent extends Event {

    private int entityID;

    public EntitySpawnEvent(int entityID) {
        this.entityID = entityID;
    }

    public int getEntityID() {
        return entityID;
    }
}
