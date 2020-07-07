package world.entities.components;

import network.MPServer;
import org.json.simple.JSONObject;
import world.events.event.ComponentStateChangedEvent;

public class HealthComponent extends Component {

    private double value, max;

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("value", value);
        object.put("max", max);
        return object;
    }

    @Override
    public void deserialize(JSONObject object) {
        value = (double)object.getOrDefault("value", 100);
        max = (double)object.getOrDefault("max", 100);
    }

    public double getValue() {
        return value;
    }

    public void setValue(int amount) {
        value = amount;
        MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
    }

    public double getMax() {
        return max;
    }

    @Override
    public String getID() {
        return "health";
    }

}
