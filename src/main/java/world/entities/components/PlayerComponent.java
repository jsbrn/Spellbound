package world.entities.components;

import org.json.simple.JSONObject;

public class PlayerComponent extends Component {
    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public JSONObject serialize() {
        return new JSONObject();
    }

    @Override
    public Component deserialize(JSONObject object) {
        return this;
    }

    @Override
    public String getID() {
        return "player";
    }
}
