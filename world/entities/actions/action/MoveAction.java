package world.entities.actions.action;

import assets.definitions.Definitions;
import world.entities.Entity;
import world.entities.actions.Action;

public class MoveAction extends Action {

    private double[] xy, target;
    private boolean lookTowards, relative;

    public MoveAction(double x, double y, boolean relative, boolean lookTowards) {
        this.xy = new double[]{x, y};
        this.lookTowards = lookTowards;
        this.relative = relative;
    }

    @Override
    public void onStart() {
        Entity parent = getParent();
        target = new double[]{
                (relative ? getParent().getLocation().getCoordinates()[0] : 0) + xy[0],
                (relative ? getParent().getLocation().getCoordinates()[1] : 0) + xy[1]};
        parent.getMover().setLookTowardsTarget(lookTowards);
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
