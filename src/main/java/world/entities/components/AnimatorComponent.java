package world.entities.components;

import misc.annotations.ServerExecution;
import network.MPServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.entities.components.animations.AnimationLayer;
import world.events.event.ComponentStateChangedEvent;

import java.util.*;

public class AnimatorComponent extends Component {

    private LinkedHashMap<String, AnimationLayer> animationLayers;

    @Override
    protected void registerEventHandlers() {

    }

    public Collection<AnimationLayer> getLayers() {
        return animationLayers.values();
    }

    public AnimationLayer getLayer(String name) {
        return animationLayers.get(name);
    }

    @ServerExecution
    public void addContext(String context) {
        for (AnimationLayer layer: animationLayers.values())
            layer.addContext(context);
        MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
    }

    @ServerExecution
    public void removeContext(String context) {
        for (AnimationLayer layer: animationLayers.values())
            layer.removeContext(context);
        MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        JSONArray layersJSON = new JSONArray();
        for (Map.Entry<String, AnimationLayer> layer: animationLayers.entrySet()) {
            JSONObject layerJSON = layer.getValue().serialize();
            layerJSON.put("name", layer.getKey());
            layersJSON.add(layerJSON);
        }
        serialized.put("layers", layersJSON);
        return serialized;
    }

    @Override
    public void deserialize(JSONObject object) {
        animationLayers = new LinkedHashMap<>();
        JSONArray layers = (JSONArray)object.getOrDefault("layers", new JSONArray());
        for (Object o: layers) {
            JSONObject jsonLayer = (JSONObject)o;
            AnimationLayer layer = new AnimationLayer();
            layer.deserialize(jsonLayer);
            animationLayers.put((String)jsonLayer.get("name"), layer);
        }
    }

    @Override
    public String getID() {
        return "animator";
    }
}
