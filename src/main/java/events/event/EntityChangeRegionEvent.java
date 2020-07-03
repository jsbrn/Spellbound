package events.event;

import world.Region;
import events.Event;

public class EntityChangeRegionEvent extends Event {

    private Region from, to;
    private Integer Integer;

    public EntityChangeRegionEvent(Region from, Region to, Integer e) {
        this.from = from;
        this.to = to;
        this.Integer = e;
    }

    public Region getFrom() {
        return from;
    }

    public Region getTo() {
        return to;
    }

    public Integer getEntity() {
        return Integer;
    }
}
