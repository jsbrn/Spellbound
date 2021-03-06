package world.entities.types;

import gui.sound.SoundManager;
import org.newdawn.slick.Sound;
import world.entities.Entity;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.actions.types.KnockbackAction;
import world.entities.animations.Animation;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityCollisionEvent;
import world.sounds.SoundEmitter;

public class SpikeTrap extends Entity {

    private int cooldown = 0;

    public SpikeTrap() {
        setIsTile(true);
        setRadius(0.25f);
        getMover().setDScore(1000);
        addSoundEmitter("attack", new SoundEmitter(1.0f, new Sound[]{SoundManager.SPIKED}, this));
        addAnimation("default", "idle", new Animation("spike_trap.png", 1, 1, 16, false, false));
        addAnimation("default", "open", new Animation("spike_trap_open.png", 8, 3, 16, false, false));
        addAnimation("default", "close", new Animation("spike_trap_close.png", 8, 3, 16, false, false));
        getAnimationLayer("default").setBaseAnimation("idle");

        SpikeTrap that = this;
        EventDispatcher.register(new EventListener().on(EntityCollisionEvent.class.toString(), new EventHandler() {
            @Override
            public void handle(Event e) {
                EntityCollisionEvent ece = (EntityCollisionEvent)e;
                if (!ece.getEntity().equals(that)) return;
                if (!(ece.getWith() instanceof HumanoidEntity)) return;
                ece.getEntity().clearAllActions();
                ece.getEntity().getActionQueue().queueAction(new ChangeAnimationAction("default", "open", true, true));
                ece.getEntity().getActionQueue().queueAction(new ChangeAnimationAction("default", "close", true, true));
                ece.getWith().clearAllActions();
                ece.getWith().getActionQueue().queueAction(new KnockbackAction(1, getLocation().angleBetween(ece.getWith().getLocation())));
                ((HumanoidEntity) ece.getWith()).addHP(-5, true);
                getSoundEmitter("attack").play();
            }
        }));

    }
}
