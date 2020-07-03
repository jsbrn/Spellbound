package events.event;

import events.Event;

public class EntityActivatedEvent extends Event {

    private Integer Integer, by;

    public EntityActivatedEvent(Integer Integer, Integer by) {
        this.Integer = Integer;
        this.by = by;
    }

    public Integer getActivatedBy() {
        return by;
    }

    public Integer getEntity() {
        return Integer;
    }

}
