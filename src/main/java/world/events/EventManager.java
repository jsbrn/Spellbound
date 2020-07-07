package world.events;

import java.util.ArrayList;

public class EventManager {

    private ArrayList<EventListener> listeners;

    public void invoke(Event e) {
        if (listeners == null) listeners = new ArrayList<>();
        for (int i = 0; i < listeners.size(); i++) {
            if (i < 0 || i >= listeners.size()) continue;
            listeners.get(i).invoke(e);
        }
        System.out.println("Event invoked: "+e.getClass().getSimpleName());
    }

    public void register(EventListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void unregister(EventListener listener) {
        if (listeners == null) return;
        listeners.remove(listener);
    }

    public void unregisterAll() {
        listeners.clear();
    }

}
