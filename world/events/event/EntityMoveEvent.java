package world.events.event;

import world.entities.Entity;
import world.events.Event;

public class EntityMoveEvent extends Event {

    private Entity entity;
    private int[] old, new_;

    public EntityMoveEvent(Entity e, double[] old_coords, double[] new_coords) {
        this.entity = e;
        this.new_ = new int[]{(int)new_coords[0], (int)new_coords[1]};
        this.old = new int[]{(int)old_coords[0], (int)old_coords[1]};
    }

    public Entity getEntity() { return entity; }

    public int[] getNewCoordinates() { return new_; }
    public int[] getOldCoordinates() { return old; }

}
