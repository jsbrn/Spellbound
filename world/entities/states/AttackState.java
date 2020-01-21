package world.entities.states;

import world.entities.Entity;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.CastSpellAction;
import world.entities.actions.action.MoveAction;
import world.entities.actions.action.SetAnimationAction;
import world.entities.actions.action.WaitAction;

import java.util.Random;

public class AttackState extends FollowState {

    private Random rng;
    private int attackDistance;

    public AttackState(Entity target, int followDistance, int attackDistance) {
        super(target, followDistance);
        this.rng = new Random();
        this.attackDistance = attackDistance;
    }

    public void update() {
        if (getParent().getActionQueue().isEmpty()
                && getParent().getLocation().distanceTo(getFollowing().getLocation()) < attackDistance
                && getParent().canSee(getFollowing()) > 0.5) {
            getParent().queueActions(rng.nextFloat() < 0.35 ? move() : cast());
        }
        super.update();
        getParent().getLocation().lookAt(getFollowing().getLocation());
    }

    private ActionGroup cast() {
        ActionGroup group = new ActionGroup();
        group.add(new CastSpellAction(getFollowing().getLocation().getCoordinates()[0], getFollowing().getLocation().getCoordinates()[1]));
        group.add(new SetAnimationAction("arms", "casting", true));
        group.add(new SetAnimationAction("arms", "default", false));
        group.add(new WaitAction(250 + rng.nextInt(500)));
        return group;
    }

    private ActionGroup move() {
        ActionGroup group = new ActionGroup();
        group.add(new SetAnimationAction("arms", "walking", false));
        group.add(new SetAnimationAction("legs", "walking", false));
        group.add(new MoveAction(-3 + rng.nextInt(6), -3 + rng.nextInt(6), true, false));
        group.add(new SetAnimationAction("arms", "default", false));
        group.add(new SetAnimationAction("legs", "default", false));
        group.add(new WaitAction(rng.nextInt(1000)));
        return group;
    }

}
