package world.entities.components;

import misc.Location;
import org.json.simple.JSONObject;
import world.events.EventDispatcher;
import world.events.EventHandler;
import world.events.EventListener;

public abstract class Component {

    private EventListener eventListener;

    protected abstract void registerEvents();

    protected EventListener on(String eventClass, EventHandler handler) {
        return eventListener.on(eventClass, handler);
    }

    public abstract JSONObject serialize();
    public abstract Component deserialize(JSONObject object);
    public abstract String getID();

    public static final Component create(String id, JSONObject defaults) {
        if (id.equals("location")) return new LocationComponent().deserialize(defaults);
        if (id.equals("hitbox")) return new HitboxComponent().deserialize(defaults);
        if (id.equals("spellbook")) return new SpellbookComponent().deserialize(defaults);
        return null;
    }

}
