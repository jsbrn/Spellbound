package world.entities.actions.action;

import assets.definitions.Definitions;
import misc.MiscMath;
import world.World;
import world.entities.Entity;
import world.entities.actions.Action;

public class MoveAction extends Action {

    private double[] target;

    public MoveAction(double x, double y) {
        this.target = new double[]{x, y};
    }

    @Override
    public void onStart() {
        Entity parent = getParent();
        double[] coordinates = parent.getLocation().getCoordinates();
        parent.getLocation().setLookDirection((int)MiscMath.angleBetween(coordinates[0], coordinates[1], target[0], target[1]));
        parent.getMover().setTarget(target[0], target[1]);
    }

    @Override
    public void onCancel() {
        getParent().getMover().stop();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        if (!getParent().getMover().isMoving()) return true;

        byte[] tile = getParent().getLocation().getRegion().getTile((int)target[0], (int)target[1]);
        if (Definitions.getTile(tile[1]).collides() || Definitions.getTile(tile[0]).collides()) return true;

        return false;
    }

    public String toString() { return "Move("+target[0]+", "+target[1]+")"; }

}
