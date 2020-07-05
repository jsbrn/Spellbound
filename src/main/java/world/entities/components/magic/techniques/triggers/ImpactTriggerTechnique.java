package world.entities.components.magic.techniques.triggers;

import world.events.Event;
import world.events.EventManager;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicImpactEvent;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class ImpactTriggerTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {
        EventManager.register(new EventListener()
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
    public void update(MagicSourceComponent cast) {

    }

}
