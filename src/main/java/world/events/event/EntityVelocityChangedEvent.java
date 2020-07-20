package world.events.event;

import world.events.Event;

public class EntityVelocityChangedEvent extends Event {

    private Integer entity;

    public EntityVelocityChangedEvent(Integer e) {
        this.entity = e;
    }

    public Integer getEntity() { return entity; }

}
