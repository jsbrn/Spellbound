package world.entities.components;

import org.json.simple.JSONObject;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;

public abstract class Component {

    private EventListener eventListener;

    protected abstract void registerEvents();

    public abstract String getID();

    protected EventListener on(String eventClass, EventHandler handler) {
        return eventListener.on(eventClass, handler);
    }

    public abstract JSONObject serialize();
    public abstract Component deserialize(JSONObject object);

}
