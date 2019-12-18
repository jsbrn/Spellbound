package world.events.event;

import world.entities.Entity;
import world.events.Event;

public class MouseReleaseEvent extends Event {

    private double x, y;

    public MouseReleaseEvent(double tx, double ty) {
        this.x = tx;
        this.y = ty;
    }

    public double getX() { return x; }
    public double getY() { return y; }

}
