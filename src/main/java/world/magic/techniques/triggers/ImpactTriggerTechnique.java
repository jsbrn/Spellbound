package world.magic.techniques.triggers;

import events.Event;
import events.EventDispatcher;
import events.EventHandler;
import events.EventListener;
import events.event.MagicImpactEvent;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class ImpactTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
                .on(MagicImpactEvent.class, new EventHandler() {
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
