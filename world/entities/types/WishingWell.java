package world.entities.types;

import org.newdawn.slick.Color;
import world.entities.Entity;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.actions.types.KnockbackAction;
import world.entities.animations.Animation;
import world.entities.states.TalkingToState;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.types.humanoids.Player;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;
import world.events.event.EntityCollisionEvent;
import world.events.event.PlayerReplyEvent;

public class WishingWell extends Entity {

    public WishingWell() {
        setRadius(0.25f);
        getMover().setCollidable(true);
        addAnimation("default", "idle", new Animation("wishing_well.png", 1, 1, 16, false, false, Color.white));
        addAnimation("default", "cast", new Animation("wishing_well_effect.png", 8, 3, 16, false, false, Color.white));
        getAnimationLayer("default").setBaseAnimation("idle");
        setConversationStartingPoint("wishing_well_greeting");
        setName("Wishing Well");
        WishingWell that = this;
        EventDispatcher.register(new EventListener()
            .on(EntityActivatedEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntityActivatedEvent ece = (EntityActivatedEvent) e;
                    if (!ece.getEntity().equals(that)) return;
                    if (!(ece.getActivatedBy() instanceof Player)) return;
                    Player activator = (Player)ece.getActivatedBy();
                    enterState(new TalkingToState(activator));
                }
            })
            .on(PlayerReplyEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    PlayerReplyEvent ece = (PlayerReplyEvent) e;
                    if (!ece.getNPC().equals(that)) return;
                }
            })
        );

    }
}
