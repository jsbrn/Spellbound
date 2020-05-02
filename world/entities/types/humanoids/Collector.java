package world.entities.types.humanoids;

import assets.SpellFactory;
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
    private int[] spellPrices = new int[]{0, 50, 25, 25, 75};
    private String[] spellTypes = new String[]{"damage", "healing", "blast", "dash"};

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
        setConversationStartingPoint("collector_introduction");
        Collector that = this;
        EventDispatcher.register(new EventListener()
            .on(PlayerReplyEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    PlayerReplyEvent pre = (PlayerReplyEvent)e;
                    if (!pre.getNPC().equals(that)) return;
                    boolean notEnough = false, inventoryFull = pre.getPlayer().getSpellbook().getSpells().size() >= 9;
                    if (pre.getDialogue().getID().equals("collector_8")) setConversationStartingPoint("collector_greeting");
                    if (pre.getDialogue().getID().equals("collector_buy_crystals")) {
                        if (pre.getPlayer().getGoldCount() >= 20) {
                            exitState();
                            getActionQueue().queueAction(new SpeakAction("Consider it done."));
                            pre.getPlayer().addCrystals(10);
                            pre.getPlayer().addGold(-20, true);
                        } else {
                            notEnough = true;
                        }
                    } else if (pre.getDialogue().getID().equals("collector_choose_spell")) {
                        if (pre.getOption() < 0) return;
                        int price = spellPrices[pre.getOption()];
                        if (pre.getPlayer().getGoldCount() < price) {
                            exitState();
                            getActionQueue().queueAction(new SpeakAction("You don't have enough money for that!"));
                            return;
                        } else {
                            exitState();
                            pre.getPlayer().getSpellbook().addSpell(SpellFactory.createSpell(spellTypes[pre.getOption()], (int)MiscMath.random(1, 2)));
                            getActionQueue().queueAction(new SpeakAction("Consider it done!"));
                        }
                    }
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
