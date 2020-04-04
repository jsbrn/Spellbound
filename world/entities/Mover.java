package world.entities;

import assets.definitions.Definitions;
import assets.definitions.TileDefinition;
import misc.Location;
import misc.MiscMath;
import world.Chunk;
import world.World;
import world.entities.pathfinding.LocalPathFinder;
import world.events.EventDispatcher;
import world.events.event.EntityMovedEvent;

import java.util.ArrayList;
import java.util.LinkedList;

public class Mover {

    private Entity parent;
    private boolean collidable, lookAtTarget;
    private boolean moving, independentAxes, ignoreCollision;
    private double[] start;
    private double speed;

    private LinkedList<Location> path;

    public Mover() {
        this.speed = 3; //tiles per second
        this.path = new LinkedList<>();
    }

    protected void setParent(Entity parent) {
        this.parent = parent;
    }

    public void setTarget(double tx, double ty) {
        if (!isIndependent() && !ignoreCollision) {
            if (!path.isEmpty()) return;
            if (canMoveDirectlyTo((int)tx + 0.5, (int)ty + 0.5))
                path.add(new Location(parent.getLocation().getRegion(), tx, ty));
            if (path.isEmpty()) path = LocalPathFinder.findPath(parent.getLocation(), (int)tx, (int)ty);
            if (path.isEmpty()) return;
        } else {
            path.clear();
            path.add(new Location(parent.getLocation().getRegion(), tx, ty));
        }

        start = parent.getLocation().getCoordinates();
        moving = true;

    }

    public void stop() {
        path.clear();
        moving = false;
    }

    public void update() {
        if (!path.isEmpty())
            moveToTarget(path.get(0).getCoordinates()[0], path.get(0).getCoordinates()[1]);
    }

    private void moveToTarget(double wx, double wy) {
        if (!moving) return;

        if (lookAtTarget) parent.getLocation().lookAt(path.get(0).getCoordinates()[0], path.get(0).getCoordinates()[1]);

        double[] coordinates = parent.getLocation().getCoordinates();
        double multiplier = Definitions.getTile(World.getRegion().getTile((int)(coordinates[0]), (int)(coordinates[1]))[1]).getSpeedMultiplier();
        double[] dir = independentAxes ? new double[]{1, 1}: MiscMath.getUnitVector(wx - start[0], wy - start[1]);

        int old_index = (int)parent.getLocation().getGlobalIndex();
        parent.getLocation().setCoordinates(
                MiscMath.tween(start[0], coordinates[0], wx, Math.abs(speed * multiplier * dir[0]), 1),
                MiscMath.tween(start[1], coordinates[1], wy, Math.abs(speed * multiplier * dir[1]), 1));
        if ((int)parent.getLocation().getGlobalIndex() != old_index) EventDispatcher.invoke(new EntityMovedEvent(parent));

        if (coordinates[0] == wx && coordinates[1] == wy) {
            if (!path.isEmpty()) path.remove(0);
            if (path.isEmpty()) moving = false;
        }

    }

    public double[] getTarget() { return path.isEmpty() ? parent.getLocation().getCoordinates() : path.get(0).getCoordinates(); }
    public LinkedList<Location> getPath() { return path; }

    public void setIndependent(boolean i) { this.independentAxes = i; }
    public boolean isIndependent() { return independentAxes; }
    public boolean isMoving() { return moving; }

    public double getSpeed() { return speed; }
    public void setSpeed(double s) { speed = s; }

    private boolean canMoveDirectlyTo(double tx, double ty) {
        double[] coords = parent.getLocation().getCoordinates();
        double dist = 1;
        int range = 1+(int)MiscMath.distance(coords[0], coords[1], tx, ty);
        double angle = MiscMath.angleBetween(coords[0], coords[1], tx, ty);
        double[] offset;
        while (dist < range) {
            offset = MiscMath.getRotatedOffset(0, -dist, angle);
            int wx = (int)(coords[0] + offset[0]), wy = (int)(coords[1] + offset[1]);
            if (!canPassThrough(wx, wy)) return false;
            dist+=0.5;
        }
        return true;
    }

    public boolean canPassThrough(double wx, double wy) {
        byte[] tile = parent.getLocation().getRegion().getTile((int)wx, (int)wy);
        TileDefinition base = Definitions.getTile(tile[0]);
        TileDefinition top = Definitions.getTile(tile[1]);

        if (base.collides() || top.collides()) return false;
        ArrayList<Entity> entities = parent.getLocation().getRegion().getEntities((int)wx, (int)wy, 1, 1);
        for (Entity e: entities) if (e.getMover().isCollidable() && !e.equals(parent)) return false;
        return true;
    }

    public double[] findMoveTarget(double dx, double dy, double maxDistance) {
        double[] coordinates = parent.getLocation().getCoordinates();
        double[] potentialTarget = new double[]{ coordinates[0], coordinates[1] };
        for (double i = 0.5; i < maxDistance; i += 0.5) {
            double tx = coordinates[0] + (i * dx);
            double ty = coordinates[1] + (i * dy);
            if (!canMoveDirectlyTo(tx, ty)) break;
            potentialTarget[0] = tx;
            potentialTarget[1] = ty;
        }
        return potentialTarget;
    }

    public void setLookTowardsTarget(boolean l) { this.lookAtTarget = l; }
    public boolean isCollidable() { return collidable; }
    public void setCollidable(boolean c) { this.collidable = c; }

    public boolean isIgnoringCollision() { return ignoreCollision; }
    public void setIgnoreCollision(boolean i) { ignoreCollision = i; }

}
