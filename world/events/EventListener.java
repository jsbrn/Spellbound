package world.events;

import world.entities.Entity;

import java.util.HashMap;

public final class EventListener {

    private HashMap<String, EventHandler> eventHandlers;

    public EventListener() {
        this.eventHandlers = new HashMap<>();
    }

    public final EventListener on(String eventClass, EventHandler eventHandler) {
        System.out.println("Registering handler for event "+eventClass);
        this.eventHandlers.put(eventClass, eventHandler);
        return this;
    }

    protected final void invoke(Event e) {
        for (String clss: eventHandlers.keySet()) {
            System.out.println(clss+" VS "+e.getClass());
            if (clss.equals(e.getClass().toString())) eventHandlers.get(clss).handle(e);
        }
    }

}
