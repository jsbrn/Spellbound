package world.entities.components;

import org.json.simple.JSONObject;

public class HealthComponent extends Component {

    private double value, max;

    @Override
    protected void registerEvents() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("value", value);
        object.put("max", max);
        return object;
    }

    @Override
    public Component deserialize(JSONObject object) {
        value = (double)object.get("value");
        max = (double)object.get("max");
        return this;
    }

    public double getValue() {
        return value;
    }

    public double getMax() {
        return max;
    }

    @Override
    public String getID() {
        return "health";
    }

}
