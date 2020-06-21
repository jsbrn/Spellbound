package world.entities.ai.states;

import misc.Location;
import misc.MiscMath;
import world.entities.ai.actions.ActionGroup;
import world.entities.ai.actions.types.MoveAction;
import world.entities.ai.actions.types.ChangeAnimationAction;
import world.entities.ai.actions.types.WaitAction;

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

            double[] new_ = MiscMath.getRotatedOffset(0, -rng.nextInt(5), rng.nextInt(360));

            ActionGroup ag = new ActionGroup();
            ag.add(new WaitAction(1000 + rng.nextInt(5000)));
            ag.add(new ChangeAnimationAction("arms", "walking", false, false));
            ag.add(new ChangeAnimationAction("legs", "walking", false, false));
            ag.add(new MoveAction(
                    original.getCoordinates()[0] + new_[0],
                    original.getCoordinates()[1] + new_[1],
                    false,
                    true));
            ag.add(new ChangeAnimationAction("arms", "default", false, false));
            ag.add(new ChangeAnimationAction("legs", "default", false, false));
            getParent().getActionQueue().queueActions(ag);
        }
    }

    @Override
    public void onExit() {

    }

}
