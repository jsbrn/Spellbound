package world.events.event;

import world.events.Event;

public class EntityCollisionEvent extends Event {

    private Entity entity, with;

    public EntityCollisionEvent(Entity entity, Entity by) {
        this.entity = entity;
        this.with = by;
    }

    public Entity getWith() {
        return with;
    }

    public Entity getEntity() {
        return entity;
    }

}
