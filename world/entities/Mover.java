package world.entities;

import assets.definitions.Definitions;
import misc.Location;
import misc.MiscMath;
import world.Chunk;
import world.Region;
import world.World;
import world.events.EventDispatcher;
import world.events.event.EntityMovedEvent;

public class Mover {

    private Entity parent;
    private double[] start;
    private boolean moving, independentAxes;
    private double targetX, targetY, speed;

    public Mover(Entity parent) {
        this.parent = parent;
        this.speed = 2; //tiles per second
    }

    public void setTarget(double tx, double ty) {
        if (targetX == tx && targetY == ty) return;
        start = parent.getLocation().getCoordinates();
        targetX = tx;
        targetY = ty;
        independentAxes = false;
        moving = true;
    }

    public void setTargetX(double tx) {
        independentAxes = true;
        start[0] = parent.getLocation().getCoordinates()[0];
        targetX = tx;
        moving = true;
    }

    public void setTargetY(double ty) {
        independentAxes = true;
        start[1] = parent.getLocation().getCoordinates()[1];
        targetY = ty;
        moving = true;
    }

    public void stop() {
        setTarget(parent.getLocation().getCoordinates()[0], parent.getLocation().getCoordinates()[1]);
    }

    public void update() {

        if (!moving) return;

        double[] coordinates = parent.getLocation().getCoordinates();
        double multiplier = Definitions.getTile(World.getRegion().getTile((int)(coordinates[0]), (int)(coordinates[1]))[1]).getSpeedMultiplier();

        double[] dir = independentAxes ? new double[]{1, 1}: MiscMath.getUnitVector(targetX - start[0], targetY - start[1]);

        int old_index = parent.getLocation().getGlobalIndex();
        parent.getLocation().setCoordinates(
                MiscMath.tween(start[0], coordinates[0], targetX, Math.abs(speed * multiplier * dir[0]), 1),
                MiscMath.tween(start[1], coordinates[1], targetY, Math.abs(speed * multiplier * dir[1]), 1));
        if (parent.getLocation().getGlobalIndex() != old_index) {
            EventDispatcher.invoke(new EntityMovedEvent(parent));
        }

        if (coordinates[0] == targetX && coordinates[1] == targetY) {
            EventDispatcher.invoke(new EntityMovedEvent(parent));
            moving = false;
        }
    }

    public double[] getTarget() { return new double[]{ targetX, targetY}; }

    public boolean isIndependent() { return independentAxes; }
    public boolean isMoving() { return moving; }

}
