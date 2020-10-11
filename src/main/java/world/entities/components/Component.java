package world.entities.components;

import misc.annotations.ServerClientExecution;
import misc.annotations.ServerExecution;
import network.Packet;
import org.json.simple.JSONObject;
import world.entities.components.magic.MagicSourceComponent;
import world.events.EventHandler;
import world.events.EventListener;

public abstract class Component {

    private Integer parent;
    private EventListener eventListener;

    @ServerExecution
    protected abstract void registerEventHandlers();

    public EventListener getEventListener() {
        return eventListener;
    }

    protected final void on(Class eventClass, EventHandler handler) {
        eventListener.on(eventClass, handler);
    }

    public abstract JSONObject serialize();
    public abstract void deserialize(JSONObject object);
    public abstract String getID();

    public Integer getParent() { return parent; }
    public void setParent(Integer entity) { parent = entity; }

    public static Component create(String id) {
        return create(id, new JSONObject());
    }

    @ServerClientExecution
    public static Component create(String id, JSONObject defaults) {
        Component c = null;
        if (id.equals("location")) c = new LocationComponent();
        if (id.equals("hitbox")) c = new HitboxComponent();
        if (id.equals("spellbook")) c = new SpellbookComponent();
        if (id.equals("health")) c = new HealthComponent();
        if (id.equals("velocity")) c = new VelocityComponent();
        if (id.equals("magic_source")) c = new MagicSourceComponent();
        if (id.equals("player")) c = new PlayerComponent();
        if (id.equals("input")) c = new InputComponent();
        if (id.equals("animator")) c = new AnimatorComponent();

        if (c != null) {
            c.deserialize(defaults);
            c.eventListener = new EventListener();
            c.registerEventHandlers();
        }
        return c;
    }

}
