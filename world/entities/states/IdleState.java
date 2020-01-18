package world.entities.states;

import misc.Location;
import world.entities.actions.action.MoveAction;
import world.entities.actions.action.SetAnimationAction;
import world.entities.actions.action.WaitAction;

import java.util.Random;

public class IdleState extends State {

    Random rng;
    Location original;

    @Override
    public void onEnter() {
        this.rng = new Random();
        Location loc = getParent().getLocation();
        this.original = new Location(
                loc.getRegion(), loc.getCoordinates()[0], loc.getCoordinates()[1]);
    }

    @Override
    public void update() {
        if (getParent().getActionQueue().isEmpty()) {
            getParent().queueAction(new WaitAction(1000 + rng.nextInt(5000)));
            getParent().queueAction(new SetAnimationAction("arms", "walking", false));
            getParent().queueAction(new SetAnimationAction("legs", "walking", false));
            getParent().queueAction(new MoveAction(
                    original.getCoordinates()[0] + (-2 + rng.nextInt(4)),
                    original.getCoordinates()[1] + (-2 + rng.nextInt(4))));
            getParent().queueAction(new SetAnimationAction("arms", "default", false));
            getParent().queueAction(new SetAnimationAction("legs", "default", false));
        }
    }

    @Override
    public void onExit() {

    }

}
