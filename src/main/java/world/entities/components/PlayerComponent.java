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
    public void deserialize(JSONObject object) {

    }

    @Override
    public String getID() {
        return "player";
    }
}
