package world.entities.ai.states;

import misc.Location;
import misc.MiscMath;
import world.entities.ai.actions.types.MoveAction;
import world.entities.ai.actions.types.ChangeAnimationAction;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityChangeRegionEvent;

import java.util.Random;

public class FollowState extends State {

    private Random rng;

    private Entity following;
    private int minimumDistance;
    private Location lastSeen;

    private float vision = 0.5f;
    private int hearing;
    private boolean allowLosing;

    private EventListener listener;

    public FollowState(Entity following, int distance, int hearing, boolean allowLosing) {
        this.following = following;
        this.lastSeen = following.getLocation();
        this.minimumDistance = distance;
        this.hearing = hearing;
        this.allowLosing = allowLosing;
        this.rng = new Random();
    }

    @Override
    public void onEnter() {

        listener = new EventListener()
                .on(EntityChangeRegionEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        if (allowLosing) return;
                        EntityChangeRegionEvent ecre = (EntityChangeRegionEvent)e;
                        if (!ecre.getEntity().equals(following)) return;
                        getParent().moveTo(new Location(following.getLocation()));
                        getParent().clearAllActions();
                        getParent().getLocation().lookAt(following.getLocation());
                    }
                });

        EventDispatcher.register(listener);
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
                if (allowLosing) {
                    getParent().exitState();
                    return;
                } else {
                    lastSeen = new Location(following.getLocation());
                }
            }
        }

        if (getParent().getActionQueue().isEmpty()) {
            double distanceTo = getParent().getLocation().distanceTo(following.getLocation());
            if (distanceTo > min + 1) {
                double[] startCoords = following.getLocation().getCoordinates();
                double[] direction = MiscMath.getRotatedOffset(0, min, getParent().getLocation().angleBetween(lastSeen));
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
        EventDispatcher.unregister(listener);
    }

}
