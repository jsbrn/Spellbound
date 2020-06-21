package world.entities.types.humanoids.npcs;

import world.entities.Entity;
import world.entities.ai.states.IdleState;
import world.entities.ai.states.TalkingToState;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.types.humanoids.Player;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;

import java.util.Random;

public class Civilian extends HumanoidEntity {

    Random rng;

    public Civilian() {
        super();
        rng = new Random();
        setName("Civilian");
        setAllegiance("player");
        Entity that = this;
        EventDispatcher.register(new EventListener()
            .on(EntityActivatedEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    if (isDead()) return;
                    EntityActivatedEvent eae = (EntityActivatedEvent)e;
                    if (eae.getEntity().equals(that) && eae.getActivatedBy() instanceof Player) {
                        enterState(new TalkingToState((Player)eae.getActivatedBy()));
                    }
                }
            })
        );

    }

    public void update() {
        super.update();
        if (getCurrentState() == null) enterState(new IdleState());
    }

}
