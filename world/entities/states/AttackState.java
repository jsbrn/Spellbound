package world.entities.states;

import misc.MiscMath;
import world.entities.Entity;
import world.entities.actions.ActionGroup;
import world.entities.actions.types.CastSpellAction;
import world.entities.actions.types.MoveAction;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.actions.types.WaitAction;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.Random;

public class AttackState extends FollowState {

    private Random rng;
    private int attackDistance;
    private int moveCount = 0;

    public AttackState(Entity target, int followDistance, int attackDistance, int hearing) {
        super(target, followDistance, hearing, true);
        this.rng = new Random();
        this.attackDistance = attackDistance;
    }

    public void update() {
        double distanceTo = getParent().getLocation().distanceTo(getFollowing().getLocation());
        if (distanceTo > attackDistance || getParent().canSee(getFollowing()) < 0.5) super.update();
        if (getParent().getActionQueue().isEmpty()
                && distanceTo < attackDistance
                && getParent().canSee(getFollowing()) > 0.5) {
            getParent().getActionQueue().queueActions(moveCount < 0 ? move() : cast());
        }
        if (((HumanoidEntity)getFollowing()).isDead() || !getFollowing().getLocation().getRegion().equals(getParent().getLocation().getRegion())) getParent().exitState();
    }

    private ActionGroup cast() {
        ActionGroup group = new ActionGroup();
        group.add(new CastSpellAction(getFollowing().getLocation().getCoordinates()[0], getFollowing().getLocation().getCoordinates()[1]));
        group.add(new ChangeAnimationAction("arms", "casting", true, true));
        group.add(new WaitAction(100 + rng.nextInt(400)));
        moveCount--;
        getParent().getLocation().lookAt(getFollowing().getLocation());
        return group;
    }

    private ActionGroup move() {
        ActionGroup group = new ActionGroup();
        group.add(new ChangeAnimationAction("arms", "walking", false, false));
        group.add(new ChangeAnimationAction("legs", "walking", false, false));
        group.add(new MoveAction(
                - 3 + rng.nextInt(6),
                - 3 + rng.nextInt(6),
                true, false));
//        group.add(new MoveAction(
//                getFollowing().getLocation().getCoordinates()[0] - attackDistance/2 + rng.nextInt(attackDistance),
//                getFollowing().getLocation().getCoordinates()[1] - attackDistance/2 + rng.nextInt(attackDistance),
//                false, false));
        group.add(new ChangeAnimationAction("arms", "default", false, false));
        group.add(new ChangeAnimationAction("legs", "default", false, false));
        group.add(new WaitAction(rng.nextInt(1000)));
        moveCount += MiscMath.random(0, 4);
        return group;
    }

}
