package world.entities.states;

import misc.Location;
import misc.MiscMath;
import world.Chunk;
import world.entities.Entity;
import world.entities.actions.action.MoveAction;
import world.entities.actions.action.SetAnimationAction;

import java.util.Random;

public class FollowState extends State {

    private Random rng;

    private Entity following;
    private int minimumDistance;
    private Location lastSeen;

    private float vision = 0.5f;
    private int hearing;

    public FollowState(Entity following, int distance, int hearing) {
        this.following = following;
        this.lastSeen = following.getLocation();
        this.minimumDistance = distance;
        this.hearing = hearing;
        this.rng = new Random();
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void update() {

        boolean canSeeTarget = getParent().canSee(following) >= vision;
        boolean canHearTarget = getParent().getLocation().distanceTo(following.getLocation()) <= hearing;
        boolean canSeeLastPosition = getParent().canSee((int)lastSeen.getCoordinates()[0], (int)lastSeen.getCoordinates()[1]) > 0.5;
        double min = canSeeLastPosition && canSeeTarget ? minimumDistance : 0;

        if (canSeeTarget || canHearTarget) {
            lastSeen = following.getLocation().copy();
        } else {
            if (getParent().getActionQueue().isEmpty()
                    && getParent().getLocation().distanceTo(following.getLocation()) >= hearing) {
                //lost em
                getParent().exitState();
                return;
            }
        }

        if (getParent().getActionQueue().isEmpty()) {
            double distanceTo = getParent().getLocation().distanceTo(following.getLocation());
            if (distanceTo > min + 1) {
                double[] startCoords = following.getLocation().getCoordinates();
                double[] direction = MiscMath.getRotatedOffset(0, -(distanceTo - min), getParent().getLocation().angleBetween(lastSeen) - 45 + rng.nextInt(90));
                getParent().queueAction(new SetAnimationAction("arms", "walking", false));
                getParent().queueAction(new SetAnimationAction("legs", "walking", false));
                getParent().queueAction(new MoveAction(startCoords[0] + direction[0], startCoords[1] + direction[1], false, true));
                getParent().queueAction(new SetAnimationAction("arms", "default", false));
                getParent().queueAction(new SetAnimationAction("legs", "default", false));
            }
        }

    }

    protected Entity getFollowing() { return following; }

    @Override
    public void onExit() {

    }

}
