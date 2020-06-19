package world.entities.magic.techniques.physical;

import gui.sound.SoundManager;
import com.github.mathiewz.slick.Sound;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicDepletedEvent;
import world.events.event.MagicImpactEvent;

public class CollisionTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
            .on(MagicImpactEvent.class.toString(), new EventHandler() {
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
