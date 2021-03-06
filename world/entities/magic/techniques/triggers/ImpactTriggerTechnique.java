package world.entities.magic.techniques.triggers;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicDepletedEvent;
import world.events.event.MagicImpactEvent;

public class ImpactTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
                .on(MagicImpactEvent.class.toString(), new EventHandler() {
                    @Override
                    public void handle(Event e) {
                        if (cast.getEnergy() <= 0) return;
                        MagicImpactEvent mie = (MagicImpactEvent)e;
                        if (!mie.getMagicSource().equals(cast)) return;
                        cast.affectOnce();
                    }
                })
        );
    }

    @Override
    public void update(MagicSource cast) {

    }

}
