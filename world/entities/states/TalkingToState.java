package world.entities.states;

import assets.definitions.Definitions;
import assets.definitions.DialogueDefinition;
import world.entities.types.humanoids.HumanoidEntity;
import world.entities.types.humanoids.Player;
import world.events.Event;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.event.*;

public class TalkingToState extends State {

    private Player to;
    private EventListener talker;

    public TalkingToState(Player to) {
        this.to = to;
        talker = new EventListener()
            .on(PlayerReplyEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    PlayerReplyEvent eae = (PlayerReplyEvent)e;
                    if (eae.getNPC().equals(getParent())) {

                        DialogueDefinition dd = eae.getDialogue().getOptionDestination(eae.getOption());
                        if (dd != null) {
                            EventDispatcher.invoke(new NPCSpeakEvent(getParent(), eae.getPlayer(), dd));
                        } else {
                            EventDispatcher.invoke(new ConversationEndedEvent(getParent(), eae.getPlayer()));
                            getParent().exitState();
                        }
                    }
                }
            })
            .on(HumanoidDeathEvent.class.toString(), new EventHandler() {
                @Override
                public void handle(Event e) {
                    HumanoidDeathEvent hde = (HumanoidDeathEvent)e;
                    if (hde.getHumanoid().equals(getParent()) || hde.getHumanoid().equals(to)) getParent().exitState();
                }
            });
    }

    @Override
    public void onEnter() {
        if (getParent() instanceof HumanoidEntity) {
            getParent().getAnimationLayer("arms").setBaseAnimation("default");
            getParent().getAnimationLayer("legs").setBaseAnimation("default");
            getParent().getAnimationLayer("head").setBaseAnimation("talking");
        }
        EventDispatcher.register(talker);
        EventDispatcher.invoke(new ConversationStartedEvent(getParent(), to));
        EventDispatcher.invoke(new NPCSpeakEvent(getParent(), to, Definitions.getDialogue(getParent().getConversationStartingPoint())));
    }

    @Override
    public void update() {
        getParent().getLocation().lookAt(to.getLocation());
    }

    @Override
    public void onExit() {
        if (getParent() instanceof HumanoidEntity) getParent().getAnimationLayer("head").setBaseAnimation("default");
        EventDispatcher.unregister(talker);
        EventDispatcher.invoke(new ConversationEndedEvent(getParent(), to));
    }

}
