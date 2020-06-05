package world.entities.actions.types;

import misc.MiscMath;
import world.Tiles;
import world.entities.Entity;
import world.entities.actions.Action;
import world.entities.types.humanoids.HumanoidEntity;

public class KnockbackAction extends Action {

    private double[] target;
    private double direction, force;
    private boolean previouslyIndependent;

    public KnockbackAction(double force, double direction) {
        this.direction = direction;
        this.force = force;
    }

    @Override
    public void onStart() {
        Entity parent = getParent();
        if (!(parent instanceof HumanoidEntity)) return;
        parent.getMover().setSpeed(force * 2);
        previouslyIndependent = parent.getMover().isIndependent();
        parent.getMover().setIndependent(false);
        parent.getMover().setIgnoreCollision(true);
        getParent().getMover().stop();
        double[] offset = MiscMath.getRotatedOffset(0, -force, direction);
        target = new double[]{
                getParent().getLocation().getCoordinates()[0] + offset[0],
                getParent().getLocation().getCoordinates()[1] + offset[1]};
        parent.getMover().setLookTowardsTarget(false);
        double[] nearestOpen = parent.getMover().findMoveTarget(offset[0], offset[1], force);
        parent.getMover().setTarget(nearestOpen[0], nearestOpen[1]);
        parent.getLocation().setLookDirection((int)((direction + 180 ) % 360));
        parent.getAnimationLayer("arms").setBaseAnimation("falling");
        parent.getAnimationLayer("legs").setBaseAnimation("falling");
        parent.getAnimationLayer("arms").stackAnimation("pushed");
        parent.getAnimationLayer("legs").stackAnimation("pushed");
    }

    @Override
    public void onCancel() {
        getParent().getMover().stop();
        if (getParent() instanceof HumanoidEntity) {
            getParent().getAnimationLayer("arms").setBaseAnimation("default");
            getParent().getAnimationLayer("legs").setBaseAnimation("default");
        }
        getParent().getMover().setIndependent(previouslyIndependent);
        getParent().getMover().setIgnoreCollision(false);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        if (!getParent().getMover().isMoving()) return true;

        byte[] tile = getParent().getLocation().getRegion().getTile((int)target[0], (int)target[1]);
        if (Tiles.collides(tile[1]) || Tiles.collides(tile[0])) return true;

        return false;
    }

    @Override
    public void onFinish() {
        if (getParent() instanceof HumanoidEntity) {
            getParent().getAnimationLayer("arms").setBaseAnimation("default");
            getParent().getAnimationLayer("legs").setBaseAnimation("default");
        }
        getParent().getMover().setIndependent(previouslyIndependent);
        getParent().getMover().setIgnoreCollision(false);
    }

    public String toString() { return "Move("+target[0]+", "+target[1]+")"; }

}
