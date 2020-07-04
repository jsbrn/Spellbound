package world.events.event;

import world.events.Event;

public class EntityMovedEvent extends Event {

    private Integer Integer;

    public EntityMovedEvent(Integer e) {
        this.Integer = e;
    }

    public Integer getEntity() { return Integer; }

}
