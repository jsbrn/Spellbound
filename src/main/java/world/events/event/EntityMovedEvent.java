package world.events.event;

import world.events.Event;

public class EntityMovedEvent extends Event {

    private Entity entity;

    public EntityMovedEvent(Entity e) {
        this.entity = e;
    }

    public Entity getEntity() { return entity; }

}
