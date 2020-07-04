package world.magic.techniques.physical;

import world.events.Event;
import world.events.EventManager;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicDepletedEvent;
import world.events.event.MagicImpactEvent;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class CollisionTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventManager.register(new EventListener()
            .on(MagicImpactEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    MagicImpactEvent mie = (MagicImpactEvent)e;
                    if (!mie.getMagicSource().equals(cast)) return;
                    EventManager.invoke(new MagicDepletedEvent(cast));
                    cast.getBody().stop();
                    cast.setMoveSpeed(0);
                }
            })
        );
    }

    @Override
    public void update(MagicSource cast) {

    }
}
