package world.entities.components.magic.techniques.triggers;

import world.events.Event;
import world.events.EventManager;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicDepletedEvent;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class DepletionTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {
        EventManager.register(new EventListener()
            .on(MagicDepletedEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    MagicDepletedEvent mde = (MagicDepletedEvent)e;
                    if (mde.getMagicSource().equals(cast)) mde.getMagicSource().affectOnce();
                }
            })
        );
    }

    @Override
    public void update(MagicSourceComponent cast) {

    }

}
