package world.events.event;

import world.events.Event;

public class MousePressedEvent extends Event {

    private double x, y;
    private int button;

    public MousePressedEvent(double tx, double ty, int button) {
        this.x = tx;
        this.y = ty;
        this.button = button;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getButton() { return button; }

}
