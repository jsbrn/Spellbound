package world.entities.types;

import gui.sound.SoundManager;
import world.entities.Entity;
import world.entities.ai.actions.types.SpeakAction;
import world.entities.animations.Animation;
import world.entities.ai.states.TalkingToState;
import world.entities.types.humanoids.Player;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;
import world.events.event.PlayerReplyEvent;

public class WishingWell extends Entity {

    public WishingWell() {
        setRadius(0.25f);
        getMover().setCollidable(true);
        addAnimation("default", "idle", new Animation("wishing_well.png", 1, 1, 16, false, false));
        addAnimation("default", "cast", new Animation("wishing_well_effect.png", 8, 3, 16, false, false));
        getAnimationLayer("default").setBaseAnimation("idle");
        setConversationStartingPoint("wishing_well_initial_greeting");
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
                    int hp_cost = 250, mana_cost = 100;
                    if (ece.getDialogue().getID().equals("wishing_well_forgot")) {
                        setConversationStartingPoint("wishing_well_greeting");
                    } else if (ece.getDialogue().getID().equals("request_mana") && ece.getOption() == 0) {
                        if (ece.getPlayer().getGoldCount() < mana_cost) {
                            enterState(null);
                            getActionQueue().queueAction(new SpeakAction("Come back when you actually have the money!"));
                        } else {
                            getActionQueue().queueAction(new SpeakAction("Consider it done."));
                            ece.getPlayer().addGold(-mana_cost, true);
                            ece.getPlayer().setMaxMana(ece.getPlayer().getMaxMana() + 10);
                            SoundManager.playSound(SoundManager.DISCOVERY);
                        }
                    } else if (ece.getDialogue().getID().equals("request_health") && ece.getOption() == 0) {
                        if (ece.getPlayer().getGoldCount() < hp_cost) {
                            enterState(null);
                            getActionQueue().queueAction(new SpeakAction("Come back when you actually have the money!"));
                        } else {
                            getActionQueue().queueAction(new SpeakAction("Consider it done."));
                            ece.getPlayer().addGold(-hp_cost, true);
                            ece.getPlayer().setMaxHP(ece.getPlayer().getMaxHP() + 10);
                            SoundManager.playSound(SoundManager.DISCOVERY);
                        }
                    }
                }
            })
        );

    }
}
