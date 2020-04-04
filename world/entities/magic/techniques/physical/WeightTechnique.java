package world.entities.magic.techniques.physical;

import misc.MiscMath;
import world.entities.actions.action.KnockbackAction;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicImpactEvent;

public class WeightTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
            .on(MagicImpactEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    MagicImpactEvent mie = (MagicImpactEvent)e;
                    if (!mie.isEntityCollision()
                            || !mie.getMagicSource().equals(cast)) return;
                    mie.getEntity().clearActions();
                    mie.getEntity().queueAction(new KnockbackAction(
                            getLevel() + 0.5,
                            MiscMath.angleBetween(
                                    cast.getBody().getLocation().getCoordinates()[0],
                                    cast.getBody().getLocation().getCoordinates()[1],
                                    mie.getEntity().getLocation().getCoordinates()[0],
                                    mie.getEntity().getLocation().getCoordinates()[1] - 0.5)));
                }
            })
        );
    }

    @Override
    public void update(MagicSource cast) {

    }
}
