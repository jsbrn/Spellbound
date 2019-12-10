package world.events;

import java.util.ArrayList;

public class EventDispatcher {

    private static ArrayList<EventListener> listeners;

    public static void invoke(Event e) {
        if (listeners == null) listeners = new ArrayList<>();
        System.out.println("Invoking event " + e.getClass());
        for (EventListener listener: listeners) {
            listener.invoke(e);
        }
    }

    public static void register(EventListener listener) {
        System.out.println("Registering event listener "+listener);
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

}
