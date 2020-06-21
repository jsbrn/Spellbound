package world.entities.types.humanoids.npcs;

import assets.definitions.Definitions;
import org.json.simple.JSONObject;
import world.World;
import world.entities.ai.actions.types.SpeakAction;
import world.entities.ai.states.*;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityChangeRegionEvent;
import world.events.event.PlayerReplyEvent;

import java.util.Random;

public class LostCivilian extends Civilian {

    Random rng;

    public LostCivilian(int difficultyMultiplier) {
        super();
        this.setName("Lost Civilian");
        this.setConversationStartingPoint("lost_civilian_greeting");
        this.rng = new Random();
        this.enterState(new CallForHelpState());
        setAllegiance("player");
        LostCivilian that = this;
        EventDispatcher.register(new EventListener()
            .on(PlayerReplyEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    PlayerReplyEvent pre = (PlayerReplyEvent)e;
                    if (!pre.getNPC().equals(that)) return;
                    if (pre.getDialogue().getID().equals("civilian_instruct")) {
                        that.enterState(new FollowState(pre.getPlayer(), 1, Integer.MAX_VALUE, false));
                        that.setConversationStartingPoint("follow_confirmation");
                    } else if (pre.getDialogue().getID().equals("civilian_waiting")) {
                        if (pre.getOption() >= 1) that.enterState(new StayPutState());
                        that.setConversationStartingPoint("civilian_continue");
                    } else if (pre.getDialogue().getID().equals("civilian_abandoned")) {
                        that.enterState(new StayPutState());
                        that.setConversationStartingPoint("civilian_redemption");
                    } else if (pre.getDialogue().getID().equals("civilian_payment")) {
                        that.enterState(new IdleState());
                        pre.getPlayer().addGold(50 * difficultyMultiplier, true);
                        that.setConversationStartingPoint("civilian_grateful");
                    }
                }
            })
            .on(EntityChangeRegionEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntityChangeRegionEvent ecre = (EntityChangeRegionEvent)e;
                    if (ecre.getFrom() == null) return;
                    if (ecre.getEntity().equals(that) && ecre.getTo().equals(World.getRegion("world"))) {
                        clearAllActions();
                        getActionQueue().queueAction(new SpeakAction(Definitions.getDialogue("civilian_rescued").getRandomText()));
                        setConversationStartingPoint("civilian_rescued");
                        enterState(new TalkingToState(World.getLocalPlayer())); //TODO: this is hacky
                    }
                }
            })
        );

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = super.serialize();
        serialized.put("rescued", getConversationStartingPoint().equals("civilian_grateful"));
        return serialized;
    }

    @Override
    public void deserialize(JSONObject json) {
        super.deserialize(json);
        if ((boolean)json.get("rescued")) enterState(new IdleState());
    }
}
