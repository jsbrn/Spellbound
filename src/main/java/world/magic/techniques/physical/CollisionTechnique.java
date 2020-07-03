package world.magic.techniques.physical;

import events.Event;
import events.EventDispatcher;
import events.EventHandler;
import events.EventListener;
import events.event.MagicDepletedEvent;
import events.event.MagicImpactEvent;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class CollisionTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
            .on(MagicImpactEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    MagicImpactEvent mie = (MagicImpactEvent)e;
                    if (!mie.getMagicSource().equals(cast)) return;
                    EventDispatcher.invoke(new MagicDepletedEvent(cast));
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
