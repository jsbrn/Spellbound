package world.entities.types;

import org.newdawn.slick.Color;
import sun.security.provider.ConfigFile;
import world.entities.Entity;
import world.entities.actions.action.ChangeAnimationAction;
import world.entities.actions.action.KnockbackAction;
import world.entities.animations.Animation;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityCollisionEvent;

import java.util.ArrayList;
import java.util.List;

public class SpikeTrap extends Entity {

    private int cooldown = 0;

    public SpikeTrap() {
        setIsTile(true);
        setRadius(0.25f);
        getMover().setCollidable(false);
        addAnimation("default", "idle", new Animation("spike_trap.png", 1, 1, 16, false, false, Color.white));
        addAnimation("default", "open", new Animation("spike_trap_open.png", 8, 3, 16, false, false, Color.white));
        addAnimation("default", "close", new Animation("spike_trap_close.png", 8, 3, 16, false, false, Color.white));
        getAnimationLayer("default").setBaseAnimation("idle");

        SpikeTrap that = this;
        EventDispatcher.register(new EventListener().on(EntityCollisionEvent.class.toString(), new EventHandler() {
            @Override
            public void handle(Event e) {
                EntityCollisionEvent ece = (EntityCollisionEvent)e;
                if (!ece.getEntity().equals(that)) return;
                if (!(ece.getWith() instanceof HumanoidEntity)) return;
                ece.getEntity().clearActions();
                ece.getEntity().queueAction(new ChangeAnimationAction("default", "open", true, true));
                ece.getEntity().queueAction(new ChangeAnimationAction("default", "close", true, true));
                ece.getWith().clearActions();
                ece.getWith().queueAction(new KnockbackAction(1, getLocation().angleBetween(ece.getWith().getLocation())));
                ((HumanoidEntity) ece.getWith()).addHP(-1);
            }
        }));

    }
}