package world.entities.states;

import world.World;
import world.entities.Entity;
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
                        if (eae.getOption() == -1) {
                            EventDispatcher.invoke(new ConversationEndedEvent(getParent(), eae.getPlayer()));
                            getParent().exitState();
                        }
                        if (eae.getOption() == 0)
                            EventDispatcher.invoke(new NPCSpeakEvent(getParent(), eae.getPlayer(), "The current time is "+ World.getCurrentTime()+".", new String[]{}));
                    }
                }
            });
    }

    @Override
    public void onEnter() {
        EventDispatcher.register(talker);
        EventDispatcher.invoke(new ConversationStartedEvent(getParent(), to));
        EventDispatcher.invoke(new NPCSpeakEvent(getParent(), to, "Generic greeting", new String[]{}));
    }

    @Override
    public void update() {
        getParent().getLocation().lookAt(to.getLocation());
    }

    @Override
    public void onExit() {
        EventDispatcher.unregister(talker);
        EventDispatcher.invoke(new ConversationEndedEvent(getParent(), to));
    }

}
