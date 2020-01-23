package world.entities.types.humanoids.npcs;

import gui.elements.SpeechBubble;
import gui.states.GameScreen;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import world.World;
import world.entities.Entity;
import world.entities.states.IdleState;
import world.entities.states.PatrolState;
import world.entities.states.TalkingToState;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.types.humanoids.Player;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;
import world.events.event.NPCSpeakEvent;
import world.events.event.PlayerReplyEvent;

import java.util.Random;

public class Civilian extends HumanoidEntity {

    Random rng;

    public Civilian() {
        super();
        rng = new Random();

        Color shirt = new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
        getAnimationLayer("torso").setColor(shirt);
        getAnimationLayer("arms").setColor(shirt.darker());
        getAnimationLayer("head").setColor(new Color(SKIN_COLORS[rng.nextInt(SKIN_COLORS.length)]));

        Entity that = this;
        EventDispatcher.register(new EventListener()
            .on(EntityActivatedEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntityActivatedEvent eae = (EntityActivatedEvent)e;
                    if (!(getCurrentState() instanceof IdleState)) return;
                    if (eae.getEntity().equals(that) && eae.getActivatedBy() instanceof Player) {
                        enterState(new TalkingToState((Player)eae.getActivatedBy()));
                    }
                }
            })
        );

    }

    public void update() {
        super.update();
        if (getCurrentState() == null) enterState(new IdleState());
    }

}
