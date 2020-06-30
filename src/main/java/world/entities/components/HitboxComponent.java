package world.entities.components;

import org.json.simple.JSONObject;

public class HitboxComponent extends Component {

    private double radius;

    @Override
    protected void registerEvents() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        serialized.put("radius", radius);
        return serialized;
    }

    @Override
    public Component deserialize(JSONObject object) {
        this.radius = (double)object.get("radius");
        return this;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String getID() {
        return "hitbox";
    }

}
