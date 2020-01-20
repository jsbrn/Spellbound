package world.entities.states;

import misc.Location;
import world.World;
import world.entities.actions.action.MoveAction;
import world.entities.actions.action.SetAnimationAction;
import world.entities.actions.action.WaitAction;

import java.util.Random;

public class FollowPlayerState extends State {

    @Override
    public void onEnter() {

    }

    @Override
    public void update() {
        if (getParent().getActionQueue().isEmpty()) {
            double[] coords = World.getPlayer().getLocation().getCoordinates();
            getParent().queueAction(new SetAnimationAction("arms", "walking", false));
            getParent().queueAction(new SetAnimationAction("legs", "walking", false));
            getParent().queueAction(new MoveAction(coords[0], coords[1], true));
            getParent().queueAction(new SetAnimationAction("arms", "default", false));
            getParent().queueAction(new SetAnimationAction("legs", "default", false));

        }
    }

    @Override
    public void onExit() {

    }

}
