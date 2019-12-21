package world.entities.actions.action;

import assets.definitions.Definitions;
import misc.MiscMath;
import world.Region;
import world.World;
import world.entities.Entity;
import world.entities.actions.Action;
import world.events.EventDispatcher;
import world.events.event.EntityMoveEvent;

public class MoveAction extends Action {

    private double[] start, target;

    public MoveAction(double x, double y) {
        this.target = new double[]{x, y};
    }

    @Override
    public void onStart() {
        this.start = this.getParent().getLocation().getCoordinates();
    }

    @Override
    public void update() {
        Entity parent = getParent();
        double[] coordinates = parent.getLocation().getCoordinates();
        double multiplier = Definitions.getTile(World.getRegion().getTile((int)(coordinates[0] + 0.5), (int)(coordinates[1] + 0.5))[1]).getSpeedMultiplier();
        parent.getLocation().setCoordinates(
                MiscMath.tween(start[0], coordinates[0], target[0], parent.getMoveSpeed() * multiplier, 1),
                MiscMath.tween(start[1], coordinates[1], target[1], parent.getMoveSpeed() * multiplier, 1));
    }

    @Override
    public boolean finished() {

        byte[] tile = World.getRegion().getTile((int)target[0], (int)target[1]);
        if (Definitions.getTile(tile[1]).collides() || Definitions.getTile(tile[0]).collides()) return true;

        double[] coordinates = getParent().getLocation().getCoordinates();
        if (coordinates[0] == target[0] && coordinates[1] == target[1]) {
            EventDispatcher.invoke(new EntityMoveEvent(getParent()));
            return true;
        }
        return false;
    }

    public String toString() { return "Move("+target[0]+", "+target[1]+")"; }

}
