package world.entities.types;

import org.newdawn.slick.Color;
import world.entities.Entity;
import world.entities.actions.action.KnockbackAction;
import world.entities.animations.Animation;
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
        getMover().setCollidable(false);
        addAnimation("default", "idle", new Animation("spike_trap.png", 1, 1, 32, false, false, Color.white));
        addAnimation("default", "open", new Animation("spike_trap_open.png", 8, 3, 32, false, false, Color.white));
        addAnimation("default", "close", new Animation("spike_trap_close.png", 8, 3, 32, false, false, Color.white));
        getAnimationLayer("default").setBaseAnimation("idle");

        EventDispatcher.register(new EventListener().on(EntityCollisionEvent.class.toString(), new EventHandler() {
            @Override
            public void handle(Event e) {
                EntityCollisionEvent ece = (EntityCollisionEvent)e;
                if (!ece.getEntity().equals(this)) return;
                ece.getWith().clearActions();
                ece.getWith().queueAction(new KnockbackAction(2, getLocation().angleBetween(ece.getWith().getLocation())));
            }
        }));

    }
}
