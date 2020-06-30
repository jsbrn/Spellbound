package world.entities.components;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.*;

public class VelocityComponent extends Component {

    private HashMap<String, Force> forces;

    @Override
    protected void registerEvents() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        for (Entry<String, Force> entry: forces.entrySet()) {
            JSONObject jsonForce = new JSONObject();
            jsonForce.put("magnitude", entry.getValue().getMagnitude());
            jsonForce.put("deceleration", entry.getValue().getDeceleration());
            jsonForce.put("direction", entry.getValue().getDirection());
            serialized.put(entry.getKey(), jsonForce);
        }
        return serialized;
    }

    @Override
    public Component deserialize(JSONObject object) {
        forces = new HashMap<>();
        for (Object e: object.entrySet()) {
            Entry entry = (Entry)e;
        }
        return this;
    }

    @Override
    public String getID() {
        return "velocity";
    }

}

class Force {

    private double magnitude, direction, deceleration;

    public Force(double direction, double magnitude, double deceleration) {
        this.magnitude = magnitude;
        this.direction = direction;
        this.deceleration = deceleration;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public double getDirection() {
        return direction;
    }

}
