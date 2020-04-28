package world.entities.types.humanoids;

import assets.definitions.Definitions;
import misc.MiscMath;
import org.json.simple.JSONObject;
import org.newdawn.slick.Color;
import world.Chunk;
import world.World;
import world.entities.actions.types.ChangeAnimationAction;
import world.entities.actions.types.KnockbackAction;
import world.entities.actions.types.MoveAction;
import world.entities.actions.types.SpeakAction;
import world.entities.animations.Animation;
import world.entities.states.*;
import world.entities.types.humanoids.npcs.Civilian;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityChangeRegionEvent;
import world.events.event.EntityCollisionEvent;
import world.events.event.PlayerReplyEvent;

import java.util.stream.Collectors;

public class Collector extends Civilian {

    private int deals;

    public Collector() {
        setName("The Collector");
        getAnimationLayer("head").setColor(Color.white);
        getAnimationLayer("torso").setColor(Color.darkGray);
        getAnimationLayer("legs").setColor(Color.black);
        getAnimationLayer("arms").setColor(Color.darkGray);
        getAnimationLayer("shirt").setColor(Color.gray);
        getAnimationLayer("shirt").setBaseAnimation("undershirt");
        getAnimationLayer("hair").setColor(Color.darkGray);
        getAnimationLayer("hair").setBaseAnimation("hood");
        setAllegiance("collector");
        setConversationStartingPoint("collector_initial_greeting");
        Collector that = this;
        EventDispatcher.register(new EventListener()
            .on(PlayerReplyEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    PlayerReplyEvent pre = (PlayerReplyEvent)e;
                    if (!pre.getNPC().equals(that)) return;
                    if (pre.getDialogue().getID().equals("collector_pleased")) setConversationStartingPoint("collector_greeting");
                }
            })
        );
    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = super.serialize();
        serialized.put("deals", deals);
        return serialized;
    }

    @Override
    public void deserialize(JSONObject json) {
        super.deserialize(json);
        deals = (int)(long)json.get("deals");
    }
}
