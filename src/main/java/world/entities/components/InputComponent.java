package world.entities.components;

import org.json.simple.JSONObject;

import java.util.HashMap;

public class InputComponent extends Component {

    private HashMap<Integer, Boolean> keyMap;

    public InputComponent() {
        this.keyMap = new HashMap<>();
    }

    @Override
    protected void registerEventHandlers() {

    }

    public void setKey(int key, boolean down) {
        keyMap.put(key, down);
    }

    public boolean getKey(int key) {
        return keyMap.get(key) != null && keyMap.get(key);
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
        return "input";
    }

}
