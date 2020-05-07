package world.entities.types.humanoids.npcs;

import assets.SpellFactory;
import gui.menus.PopupMenu;
import gui.states.GameState;
import main.GameManager;
import misc.MiscMath;
import org.json.simple.JSONObject;
import org.newdawn.slick.Color;
import world.World;
import world.entities.actions.types.SpeakAction;
import world.entities.states.TalkingToState;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.types.humanoids.Player;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;
import world.events.event.PlayerReplyEvent;

public class Collector extends HumanoidEntity {

    private int deals;
    private int[] spellPrices = new int[]{0, 50, 25, 25, 75};
    private String[] spellTypes = new String[]{"damage", "healing", "barrier"};

    public Collector() {
        setName("The Collector");
        setMaxHP(Integer.MAX_VALUE);
        setHP(Integer.MAX_VALUE, false);
        getAnimationLayer("head").setColor(Color.white);
        getAnimationLayer("torso").setColor(Color.darkGray);
        getAnimationLayer("legs").setColor(Color.black);
        getAnimationLayer("arms").setColor(Color.darkGray);
        getAnimationLayer("shirt").setColor(Color.gray);
        getAnimationLayer("shirt").setBaseAnimation("undershirt");
        getAnimationLayer("hair").setColor(Color.darkGray);
        getAnimationLayer("hair").setBaseAnimation("hood");
        setAllegiance("player");
        setConversationStartingPoint("collector_introduction");
        Collector that = this;
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
            .on(PlayerReplyEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    /**
                     * TODO: This is horrible, horrible temporary code. Convert to proper Merchant UI/component in the future.
                     */
                    PlayerReplyEvent pre = (PlayerReplyEvent)e;
                    if (!pre.getNPC().equals(that)) return;
                    String id = pre.getDialogue().getID();
                    boolean notEnough = false, inventoryFull = pre.getPlayer().getSpellbook().getSpells().size() >= 9;
                    if (id.equals("collector_8")) setConversationStartingPoint("collector_demand");
                    if (id.equals("collector_demand") && pre.getOption() == 0) {
                        if (pre.getPlayer().getArtifactCount() < 5) {
                            exitState();
                            getActionQueue().queueAction(new SpeakAction("Liar!"));
                        } else {
                            //winner!
                            setConversationStartingPoint("collector_greeting");
                            pre.getPlayer().addArtifacts(-pre.getPlayer().getArtifactCount());
                            pre.getPlayer().setHasAmulet(true);
                            GameManager.getGameState(GameState.GAME_SCREEN).getGUI().stackModal(new PopupMenu(
                                    "Congratulations!",
                                    "You found the Amulet",
                                    "You found all 5 Artifacts and the Collector has given you the Amulet. All that's left is to find your friend and return it.",
                                    "icons/amulet.png",
                                    Color.white
                            ));
                        }
                    } else if (id.equals("collector_buy")) {
                        if (pre.getOption() == 0) {
                            if (pre.getPlayer().getGoldCount() >= 25) {
                                exitState();
                                getActionQueue().queueAction(new SpeakAction("Consider it done."));
                                pre.getPlayer().addCrystals(10);
                                pre.getPlayer().addGold(-25, true);
                            } else {
                                getActionQueue().queueAction(new SpeakAction("You don't have enough for that."));
                                return;
                            }
                        } else if (pre.getOption() == 1) {
                            if (pre.getPlayer().getGoldCount() >= 10) {
                                exitState();
                                getActionQueue().queueAction(new SpeakAction("Consider it done."));
                                pre.getPlayer().addDyes(10);
                                pre.getPlayer().addGold(-10, true);
                            } else {
                                getActionQueue().queueAction(new SpeakAction("You don't have enough for that."));
                                return;
                            }
                        }
                    } else if (id.equals("collector_sell")) {
                        if (pre.getOption() == 0) {
                            if (pre.getPlayer().getCrystalCount() >= 10) {
                                getActionQueue().queueAction(new SpeakAction("Pleasure doing business with you!"));
                                pre.getPlayer().addCrystals(-10);
                                pre.getPlayer().addGold(15, true);
                            } else {
                                getActionQueue().queueAction(new SpeakAction("Nice try."));
                                return;
                            }
                        }
                    } else if (pre.getDialogue().getID().equals("collector_choose_spell")) {
                        if (pre.getOption() < 0) return;
                        if (inventoryFull) {
                            exitState();
                            getActionQueue().queueAction(new SpeakAction("Make some room in your inventory first."));
                            return;
                        }
                        int price = spellPrices[pre.getOption()];
                        if (pre.getPlayer().getGoldCount() < price) {
                            exitState();
                            getActionQueue().queueAction(new SpeakAction("You don't have enough money for that!"));
                            return;
                        } else {
                            exitState();
                            pre.getPlayer().getSpellbook().addSpell(SpellFactory.createSpell(spellTypes[pre.getOption()], (int)MiscMath.random(1, 2)));
                            getActionQueue().queueAction(new SpeakAction("Hope it serves you well."));
                        }
                    }
                }
            })
        );
    }

    @Override
    public void update() {
        super.update();
        if (canSee(World.getLocalPlayer()) > 0 && getConversationStartingPoint().equals("collector_introduction") && getCurrentState() == null)
            enterState(new TalkingToState(World.getLocalPlayer()));
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
