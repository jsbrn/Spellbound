package world.entities.components;

import org.json.simple.JSONObject;
import world.events.EventHandler;
import world.events.EventListener;

public abstract class Component {

    private Integer parent;
    private EventListener eventListener;

    protected abstract void registerEventHandlers();

    public EventListener getEventListener() {
        return eventListener;
    }

    protected final void on(Class eventClass, EventHandler handler) {
        eventListener.on(eventClass, handler);
    }

    public abstract JSONObject serialize();
    public abstract Component deserialize(JSONObject object);
    public abstract String getID();

    public Integer getParent() { return parent; }
    public void setParent(Integer entity) { parent = entity; }

    public static Component create(String id, JSONObject defaults) {
        Component c = null;
        if (id.equals("location")) c = new LocationComponent().deserialize(defaults);
        if (id.equals("hitbox")) c = new HitboxComponent().deserialize(defaults);
        if (id.equals("spellbook")) c = new SpellbookComponent().deserialize(defaults);
        if (id.equals("health")) c = new HealthComponent().deserialize(defaults);
        if (id.equals("velocity")) c = new VelocityComponent().deserialize(defaults);

        if (c != null) {
            c.eventListener = new EventListener();
            c.registerEventHandlers();
        }
        return c;
    }

}
