package world.entities.actions.action;

import assets.definitions.Definitions;
import misc.MiscMath;
import world.entities.Entity;
import world.entities.actions.Action;
import world.entities.types.humanoids.HumanoidEntity;

public class KnockbackAction extends Action {

    private double[] target;
    private double direction, strength;

    public KnockbackAction(double strength, double direction) {
        this.direction = direction;
        this.strength = strength;
    }

    @Override
    public void onStart() {
        Entity parent = getParent();
        double[] offset = MiscMath.getRotatedOffset(0, -strength, direction);
        target = new double[]{
                getParent().getLocation().getCoordinates()[0] + offset[0],
                getParent().getLocation().getCoordinates()[1] + offset[1]};
        parent.getMover().setLookTowardsTarget(false);
        double[] nearestOpen = parent.getMover().findMoveTarget(offset[0], offset[1], strength);
        parent.getMover().setTarget(nearestOpen[0], nearestOpen[1]);
        parent.getLocation().setLookDirection((int)((direction + 180 ) % 360));
        if (parent instanceof HumanoidEntity) {
            parent.getAnimationLayer("arms").setBaseAnimation("falling");
            parent.getAnimationLayer("legs").setBaseAnimation("falling");
            parent.getAnimationLayer("arms").stackAnimation("pushed");
            parent.getAnimationLayer("legs").stackAnimation("pushed");
        }
    }

    @Override
    public void onCancel() {
        getParent().getMover().stop();
        if (getParent() instanceof HumanoidEntity) {
            getParent().getAnimationLayer("arms").setBaseAnimation("default");
            getParent().getAnimationLayer("legs").setBaseAnimation("default");
        }
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

    @Override
    public void onFinish() {
        if (getParent() instanceof HumanoidEntity) {
            getParent().getAnimationLayer("arms").setBaseAnimation("default");
            getParent().getAnimationLayer("legs").setBaseAnimation("default");
        }
    }

    public String toString() { return "Move("+target[0]+", "+target[1]+")"; }

}
