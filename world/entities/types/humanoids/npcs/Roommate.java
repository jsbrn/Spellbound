package world.entities.types.humanoids.npcs;

import gui.menus.PopupMenu;
import gui.states.GameState;
import main.GameManager;
import org.newdawn.slick.Color;
import world.World;
import world.entities.Entity;
import world.entities.actions.types.SpeakAction;
import world.entities.states.IdleState;
import world.entities.states.TalkingToState;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.types.humanoids.Player;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.EntityActivatedEvent;
import world.events.event.PlayerReplyEvent;

import java.util.Random;

public class Roommate extends HumanoidEntity {

    Random rng;

    public Roommate() {
        super();
        rng = new Random();
        setName("Roommate");
        setAllegiance("player");
        setConversationStartingPoint("roommate_introduction");
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
            .on(PlayerReplyEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    PlayerReplyEvent pre = (PlayerReplyEvent)e;
                    if (pre.getDialogue().getID().equals("roommate_5")) {
                        setConversationStartingPoint("roommate_greeting");
                    } else if (pre.getDialogue().getID().equals("roommate_greeting")) {
                        if (pre.getOption() == 0) {
                            exitState();
                            if (pre.getPlayer().hasAmulet()) {
                                //you did it!
                                pre.getPlayer().setHasAmulet(false);
                                setConversationStartingPoint("roommate_satisfied");
                                GameManager.getGameState(GameState.GAME_SCREEN).getGUI().stackModal(new PopupMenu(
                                        "Victory",
                                        "Thanks for playing!",
                                        "You've successfully returned the Amulet, and the game is now complete. " +
                                                "But don't worry. There are still plenty of dungeons to loot and spells to discover. " +
                                                "I am working on a much larger main quest, and much more." +
                                                "Stay tuned!",
                                        "icons/amulet.png",
                                        Color.white
                                ));
                            } else {
                                getActionQueue().queueAction(new SpeakAction("Liar!"));
                            }
                        }
                    }
                }
            })
        );
    }

    @Override
    public void update() {
        super.update();
        if (getConversationStartingPoint().equals("roommate_introduction") && getCurrentState() == null) {
            enterState(new TalkingToState(World.getLocalPlayer()));
        }
    }

}
