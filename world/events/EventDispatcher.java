package world.events;

import java.util.ArrayList;

public class EventDispatcher {

    private static ArrayList<EventListener> listeners;

    public static void invoke(Event e) {
        if (listeners == null) listeners = new ArrayList<>();
        for (EventListener listener: listeners) {
            listener.invoke(e);
        }
    }

    public static void register(EventListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

}
