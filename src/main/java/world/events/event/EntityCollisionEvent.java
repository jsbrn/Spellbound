package world.events.event;

import world.events.Event;

public class EntityCollisionEvent extends Event {

    private Integer Integer, with;

    public EntityCollisionEvent(Integer Integer, Integer by) {
        this.Integer = Integer;
        this.with = by;
    }

    public Integer getWith() {
        return with;
    }

    public Integer getEntity() {
        return Integer;
    }

}
