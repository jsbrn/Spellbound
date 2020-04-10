package world.entities.states;

import misc.Location;
import misc.MiscMath;
import world.entities.Entity;
import world.entities.actions.types.MoveAction;
import world.entities.actions.types.ChangeAnimationAction;

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
            lastSeen = new Location(following.getLocation());
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
                getParent().getActionQueue().queueAction(new ChangeAnimationAction("arms", "walking", false, false));
                getParent().getActionQueue().queueAction(new ChangeAnimationAction("legs", "walking", false, false));
                getParent().getActionQueue().queueAction(new MoveAction(startCoords[0] + direction[0], startCoords[1] + direction[1], false, true));
                getParent().getActionQueue().queueAction(new ChangeAnimationAction("arms", "default", false, false));
                getParent().getActionQueue().queueAction(new ChangeAnimationAction("legs", "default", false, false));
            }
        }

    }

    protected Entity getFollowing() { return following; }

    @Override
    public void onExit() {

    }

}
