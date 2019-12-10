package world.entities.actions.action;

import misc.MiscMath;
import world.entities.Entity;
import world.entities.actions.Action;
import world.events.EventDispatcher;
import world.events.event.EntityMoveEvent;

public class MoveAction extends Action {

    private double walk_speed;
    private double[] start, target;

    public MoveAction(double x, double y, double walk_speed) {
        this.target = new double[]{x, y};
        this.walk_speed = walk_speed;
    }

    @Override
    public void init() {
        this.start = this.getParent().getCoordinates();
    }

    @Override
    public void update() {
        Entity parent = getParent();
        double[] coordinates = parent.getCoordinates();
        parent.setCoordinates(
                MiscMath.tween(start[0], coordinates[0], target[0], walk_speed, 1),
                MiscMath.tween(start[1], coordinates[1], target[1], walk_speed, 1));
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean finished() {
        double[] coordinates = getParent().getCoordinates();
        if (coordinates[0] == target[0] && coordinates[1] == target[1]) {
            EventDispatcher.invoke(new EntityMoveEvent(getParent()));
            return true;
        }
        return false;
    }

}
