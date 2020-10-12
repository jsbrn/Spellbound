package world.entities.components;

import misc.annotations.ServerClientExecution;
import misc.annotations.ServerExecution;
import network.MPServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.entities.components.animations.Animation;
import world.entities.components.animations.AnimationLayer;
import world.events.event.ComponentStateChangedEvent;

import java.util.*;

public class AnimatorComponent extends Component {

    private ArrayList<String> activeAnimations;
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
        if (!activeAnimations.contains(context)) {
            activeAnimations.add(context);
            MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
        }
    }

    @ServerExecution
    public void removeContext(String context) {
        if (activeAnimations.contains(context)) {
            activeAnimations.remove(context);
            MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
        }
    }

    @ServerClientExecution
    public void cleanExpiredContexts() {
        for (int i = activeAnimations.size() - 1; i > -1; i--) {
            boolean isContextFinished = true;
            for (AnimationLayer al: animationLayers.values()) {
                Animation a = al.getAnimation(activeAnimations.get(i));
                if (a == null) continue;
                if (a.loops() || a.finished()) isContextFinished = false;
            }
            if (isContextFinished) activeAnimations.remove(i);
        }
    }

    public ArrayList<String> getActiveAnimations() {
        return activeAnimations;
    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        JSONArray layersJSON = new JSONArray();
        //save animation layers
        for (Map.Entry<String, AnimationLayer> layer: animationLayers.entrySet()) {
            JSONObject layerJSON = layer.getValue().serialize();
            layerJSON.put("name", layer.getKey());
            layersJSON.add(layerJSON);
        }
        serialized.put("layers", layersJSON);

        //save active animations
        JSONArray jsonActiveAnimations = new JSONArray();
        for (String s: activeAnimations) jsonActiveAnimations.add(s);
        serialized.put("active_animations", jsonActiveAnimations);


        return serialized;
    }

    @Override
    public void deserialize(JSONObject object) {
        animationLayers = new LinkedHashMap<>();
        activeAnimations = new ArrayList<>();
        //load animation layers
        JSONArray layers = (JSONArray)object.getOrDefault("layers", new JSONArray());
        for (Object o: layers) {
            JSONObject jsonLayer = (JSONObject)o;
            AnimationLayer layer = new AnimationLayer();
            layer.deserialize(jsonLayer);
            animationLayers.put((String)jsonLayer.get("name"), layer);
        }
        //load active animations
        JSONArray jsonContexts = (JSONArray)object.getOrDefault("active_animations", new JSONArray());
        for (Object o: jsonContexts) activeAnimations.add((String)o);

    }

    @Override
    public String getID() {
        return "animator";
    }
}
