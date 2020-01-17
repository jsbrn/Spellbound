package world.entities.actions.action;

import assets.definitions.Definitions;
import misc.MiscMath;
import world.World;
import world.entities.Entity;
import world.entities.actions.Action;

public class MoveAction extends Action {

    private double[] start, target, vel;

    public MoveAction(double x, double y) {
        this.target = new double[]{x, y};
    }

    @Override
    public void onStart() {
        Entity parent = getParent();
        double[] coordinates = parent.getLocation().getCoordinates();
        parent.getLocation().setLookDirection((int)MiscMath.angleBetween(coordinates[0], coordinates[1], target[0], target[1]));
        this.start = coordinates;
        this.vel = MiscMath.getUnitVector(target[0] - start[0], target[1] - start[1]);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {

        byte[] tile = World.getRegion().getTile((int)target[0], (int)target[1]);
        if (Definitions.getTile(tile[1]).collides() || Definitions.getTile(tile[0]).collides()) return true;

        double[] coordinates = getParent().getLocation().getCoordinates();

        return false;
    }

    public String toString() { return "Move("+target[0]+", "+target[1]+")"; }

}
