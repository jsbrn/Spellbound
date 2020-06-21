package world.entities.types.humanoids.enemies;

import gui.sound.SoundManager;
import misc.MiscMath;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Sound;
import world.Chunk;
import world.entities.ai.actions.types.ChangeAnimationAction;
import world.entities.ai.actions.types.KnockbackAction;
import world.entities.ai.actions.types.MoveAction;
import world.entities.animations.Animation;
import world.entities.ai.states.PatrolState;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityCollisionEvent;
import world.events.event.HumanoidDamageEvent;
import world.events.event.HumanoidDeathEvent;
import world.sounds.SoundEmitter;

import java.util.stream.Collectors;

public class Zombie extends HumanoidEntity {

    private HumanoidEntity target;

    public Zombie() {
        getAnimationLayer("arms").addAnimation("outstretched", new Animation("humanoid/arms_outstretched.png", 1, 1, 16, true, true));
        getAnimationLayer("head").setColor(Color.green.darker(0.75f));
        getAnimationLayer("torso").setColor(Color.orange.darker());
        getAnimationLayer("legs").setColor(Color.cyan.darker());
        getAnimationLayer("arms").setColor(Color.orange.darker(0.75f));
        getAnimationLayer("shirt").setBaseAnimation("dirty");
        getAnimationLayer("hair").setColor(Color.black);

        addSoundEmitter("idle", new SoundEmitter(5000, 7000, 0.4f, new Sound[]{SoundManager.ZOMBIE_IDLE_1, SoundManager.ZOMBIE_IDLE_2}, this));
        addSoundEmitter("death", new SoundEmitter(1.0f, new Sound[]{SoundManager.ZOMBIE_DEATH}, this));
        addSoundEmitter("pain", new SoundEmitter(1.0f, new Sound[]{SoundManager.ZOMBIE_PAIN}, this));
        addSoundEmitter("bite", new SoundEmitter(1.0f, new Sound[]{SoundManager.ZOMBIE_BITE}, this));

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
                            1,
                            MiscMath.angleBetween(
                                    getLocation().getCoordinates()[0],
                                    getLocation().getCoordinates()[1],
                                    ece.getWith().getLocation().getCoordinates()[0],
                                    ece.getWith().getLocation().getCoordinates()[1])));
                    ((HumanoidEntity)ece.getWith()).addHP(-5, true);
                    getActionQueue("arms").queueAction(new ChangeAnimationAction("arms", "casting", true, true));
                    getLocation().lookAt(ece.getWith().getLocation());
                    getSoundEmitter("bite").play();
                }
            })
            .on(HumanoidDeathEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    HumanoidDeathEvent hde = (HumanoidDeathEvent)e;
                    if (hde.getHumanoid().equals(that)) {
                        getSoundEmitter("idle").setActive(false);
                        //getSoundEmitter("death").play();
                    }
                }
            })
            .on(HumanoidDamageEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    HumanoidDamageEvent hde = (HumanoidDamageEvent)e;
                    if (hde.getHumanoid().equals(that)) {
                        getSoundEmitter("idle").stop();
                        getSoundEmitter("pain").play();
                    }
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
                enterState(new PatrolState(5));
            }

        }
    }
}
