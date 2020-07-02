package world.magic.techniques.physical;

import com.github.mathiewz.slick.Sound;
import gui.sound.SoundManager;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.MagicImpactEvent;
import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class WeightTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        EventDispatcher.register(new EventListener()
            .on(MagicImpactEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    MagicImpactEvent mie = (MagicImpactEvent)e;
                    if (!mie.isEntityCollision()
                            || !mie.getMagicSource().equals(cast)) return;

                    double force = ((double)Math.max(1, cast.getLevel("physical_speed")) * ((double)cast.getLevel("physical_weight") / 5.0) / 2.0) + 0.5;

                    Sound s = force <= 1 ? SoundManager.IMPACT_1 : (force > 2.5 ? SoundManager.IMPACT_3 : SoundManager.IMPACT_2);
                    //SoundManager.playSound(s, 0.6f, mie.getEntity().getLocation());

                    /*mie.getEntity().clearAllActions();
                    mie.getEntity().getActionQueue().queueAction(new KnockbackAction(
                            force,
                            MiscMath.angleBetween(
                                    cast.getBody().getLocation().getCoordinates()[0],
                                    cast.getBody().getLocation().getCoordinates()[1],
                                    mie.getEntity().getLocation().getCoordinates()[0],
                                    mie.getEntity().getLocation().getCoordinates()[1])));*/
                }
            })
        );
    }

    @Override
    public void update(MagicSource cast) {

    }
}
