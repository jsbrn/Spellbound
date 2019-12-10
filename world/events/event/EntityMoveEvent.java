package world.events.event;

import world.entities.Entity;
import world.events.Event;

public class EntityMoveEvent extends Event {

    private Entity entity;

    public EntityMoveEvent(Entity e) {
        this.entity = e;
    }

    public Entity getEntity() { return entity; }

}
