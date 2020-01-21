package world.entities.states;

import world.entities.Entity;
import world.entities.actions.ActionGroup;
import world.entities.actions.action.CastSpellAction;
import world.entities.actions.action.SetAnimationAction;
import world.entities.actions.action.WaitAction;

public class AttackState extends FollowState {

    private int attackDistance;

    public AttackState(Entity target, int followDistance, int attackDistance) {
        super(target, followDistance);
        this.attackDistance = attackDistance;
    }

    public void update() {
        if (getParent().getActionQueue().isEmpty()
                && getParent().getLocation().distanceTo(getFollowing().getLocation()) < attackDistance
                && getParent().canSee(getFollowing()) > 0.5) {
            //attack!!!!!
            ActionGroup group = new ActionGroup();
            group.add(new CastSpellAction(getFollowing().getLocation().getCoordinates()[0], getFollowing().getLocation().getCoordinates()[1]));
            group.add(new SetAnimationAction("arms", "casting", true));
            group.add(new SetAnimationAction("arms", "default", false));
            group.add(new WaitAction(500));
            getParent().queueActions(group);
            getParent().getLocation().lookAt(getFollowing().getLocation());
        }
        super.update();
    }

}
