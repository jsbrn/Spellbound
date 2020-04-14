package world.events.event;

import world.Region;
import world.entities.Entity;
import world.events.Event;

public class EntityChangeRegionEvent extends Event {

    private Region from, to;
    private Entity entity;

    public EntityChangeRegionEvent(Region from, Region to, Entity e) {
        this.from = from;
        this.to = to;
        this.entity = e;
    }

    public Region getFrom() {
        return from;
    }

    public Region getTo() {
        return to;
    }

    public Entity getEntity() {
        return entity;
    }
}
