package world.events;

import java.util.ArrayList;

public class EventDispatcher {

    private static ArrayList<EventListener> listeners;

    public static void invoke(Event e) {
        if (listeners == null) listeners = new ArrayList<>();
        for (int i = 0; i < listeners.size(); i++) {
            if (i < 0 || i >= listeners.size()) continue;
            listeners.get(i).invoke(e);
        }
    }

    public static void register(EventListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public static void unregister(EventListener listener) {
        if (listeners == null) return;
        listeners.remove(listener);
    }

}
