package world.events;

import java.util.HashMap;

public class EventListener {

    private HashMap<Class, EventHandler> eventHandlers;

    public EventListener() {
        this.eventHandlers = new HashMap<>();
    }

    public final EventListener on(Class eventClass, EventHandler eventHandler) {
        this.eventHandlers.put(eventClass, eventHandler);
        return this;
    }

    protected final void invoke(Event e) {
        for (Class eventClass : eventHandlers.keySet()) {
            if (eventClass.equals(e.getClass())) eventHandlers.get(eventClass).handle(e);
        }
    }

}
