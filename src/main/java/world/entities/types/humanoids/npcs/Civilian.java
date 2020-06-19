package world.entities.types.humanoids.npcs;

import gui.elements.SpeechBubble;
import gui.states.GameScreen;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Game;
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
import world.events.event.EntityChangeRegionEvent;
import world.events.event.NPCSpeakEvent;
import world.events.event.PlayerReplyEvent;

import java.util.Random;

public class Civilian extends HumanoidEntity {

    Random rng;

    public Civilian() {
        super();
        rng = new Random();
        setName("Civilian");
        setAllegiance("player");
        Entity that = this;
        EventDispatcher.register(new EventListener()
            .on(EntityActivatedEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    if (isDead()) return;
                    EntityActivatedEvent eae = (EntityActivatedEvent)e;
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
