package world.entities.magic.techniques.triggers;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicDepletedEvent;

public class DepletionTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
            .on(MagicDepletedEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    cast.affectOnce();
                }
            })
        );
    }

    @Override
    public void update(MagicSource cast) {

    }

}
