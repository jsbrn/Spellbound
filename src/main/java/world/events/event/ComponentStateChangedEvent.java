package world.events.event;

import network.packets.ComponentStateChangePacket;
import org.json.simple.JSONObject;
import world.entities.components.Component;
import world.events.Event;

public class ComponentStateChangedEvent extends Event {

    private Component component;

    public ComponentStateChangedEvent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
