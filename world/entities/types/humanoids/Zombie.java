package world.entities.types.humanoids;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.Chunk;
import world.entities.actions.Action;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.actions.types.KnockbackAction;
import world.entities.actions.types.MoveAction;
import world.entities.animations.Animation;
import world.entities.states.FollowState;
import world.entities.states.PatrolState;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityCollisionEvent;

import java.util.stream.Collectors;

public class Zombie extends HumanoidEntity {

    private HumanoidEntity target;

    public Zombie() {
        getAnimationLayer("arms").addAnimation("outstretched", new Animation("humanoid/arms_outstretched.png", 1, 1, 16, true, true, Color.white));
        getAnimationLayer("head").setColor(Color.green.darker(0.75f));
        getAnimationLayer("torso").setColor(Color.orange.darker());
        getAnimationLayer("legs").setColor(Color.cyan.darker());
        getAnimationLayer("arms").setColor(Color.orange.darker(0.75f));
        setAllegiance("undead");
        Zombie that = this;
        EventDispatcher.register(new EventListener()
            .on(EntityCollisionEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntityCollisionEvent ece = (EntityCollisionEvent)e;
                    if (!ece.getEntity().equals(that)) return;
                    if (!(ece.getWith() instanceof HumanoidEntity) || ((HumanoidEntity) ece.getWith()).isAlliedTo(that) || ((HumanoidEntity) ece.getWith()).isDead()) return;
                    ece.getWith().clearAllActions();
                    ece.getWith().getActionQueue().queueAction(new KnockbackAction(
                            1.5,
                            MiscMath.angleBetween(
                                    getLocation().getCoordinates()[0],
                                    getLocation().getCoordinates()[1],
                                    ece.getWith().getLocation().getCoordinates()[0],
                                    ece.getWith().getLocation().getCoordinates()[1])));
                    ((HumanoidEntity)ece.getWith()).addHP(-1);
                    getActionQueue("arms").queueAction(new ChangeAnimationAction("arms", "casting", true, true));
                    getLocation().lookAt(ece.getWith().getLocation());
                }
            })
        );
    }

    @Override
    public void update() {
        super.update();
        if (!isDead()) getAnimationLayer("arms").setBaseAnimation("outstretched");
        if (target == null) {
            getNearbyEntities(12).stream()
                .filter(e ->
                        e instanceof HumanoidEntity &&
                        !((HumanoidEntity) e).isAlliedTo(this) &&
                        canSee(e) > 0.5 &&
                        !((HumanoidEntity) e).isDead())
                .map(e -> (HumanoidEntity)e)
                .findFirst().ifPresent(e -> {
                    target = e;
                });
            }
        if (getActionQueue().isEmpty() && target != null) {
            exitState();
            double[] relative = MiscMath.getRotatedOffset(0, -MiscMath.random(0.5, 1.5),
                    MiscMath.angleBetween(
                            getLocation().getCoordinates()[0],
                            getLocation().getCoordinates()[1],
                            target.getLocation().getCoordinates()[0],
                            target.getLocation().getCoordinates()[1]));
            if (getLocation().distanceTo(target.getLocation()) >= 1) {
                if (getLocation().getRegion().getEntities(getLocation().getCoordinates()[0] + relative[0], getLocation().getCoordinates()[1], 0.25)
                        .stream().filter(e -> !e.equals(this)).collect(Collectors.toList()).isEmpty()
                        || getLocation().distanceTo(target.getLocation()) < 2) {
                    getActionQueue().queueAction(new ChangeAnimationAction("legs", "walking", false, false));
                    getActionQueue().queueAction(new MoveAction(relative[0], relative[1], true, true));
                    getActionQueue().queueAction(new ChangeAnimationAction("legs", "default", false, false));
                }
            }
            if (canSee(target) < 0.25 || getLocation().distanceTo(target.getLocation()) > Chunk.CHUNK_SIZE * 2 || target.isDead()) {
                target = null;
                enterState(new PatrolState(6));
            }

        }
    }
}
