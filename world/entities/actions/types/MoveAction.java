package world.entities.actions.types;

import world.Tiles;
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
        parent.getMover().setSpeed(3);
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
        if (Tiles.collides(tile[0]) || Tiles.collides(tile[1]) || tile[0] == Tiles.AIR) return true;

        return false;
    }

    @Override
    public void onFinish() {
        getParent().getMover().stop();
    }

    public String toString() { return "Move("+target[0]+", "+target[1]+")"; }

}
