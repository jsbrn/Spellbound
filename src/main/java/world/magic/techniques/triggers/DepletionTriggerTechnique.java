package world.magic.techniques.triggers;

import events.Event;
import events.EventDispatcher;
import events.EventHandler;
import events.EventListener;
import events.event.MagicDepletedEvent;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class DepletionTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
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
    public void update(MagicSource cast) {

    }

}
