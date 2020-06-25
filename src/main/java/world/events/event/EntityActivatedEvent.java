package world.events.event;

import world.events.Event;

public class EntityActivatedEvent extends Event {

    private Entity entity, by;

    public EntityActivatedEvent(Entity entity, Entity by) {
        this.entity = entity;
        this.by = by;
    }

    public Entity getActivatedBy() {
        return by;
    }

    public Entity getEntity() {
        return entity;
    }

}
