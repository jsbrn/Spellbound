package world.entities.components;

import com.github.mathiewz.slick.Color;
import misc.Colors;
import misc.MiscMath;
import misc.annotations.ClientExecution;
import misc.annotations.ServerExecution;
import network.MPServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.entities.components.animations.AnimationLayer;
import world.events.event.ComponentStateChangedEvent;
import world.events.event.EntityChangedAnimationEvent;

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
            MPServer.getEventManager().invoke(new EntityChangedAnimationEvent(this));
        }
    }

    @ServerExecution
    public void removeContext(String context) {
        if (activeAnimations.contains(context)) {
            activeAnimations.remove(context);
            MPServer.getEventManager().invoke(new EntityChangedAnimationEvent(this));
        }
    }

    @ServerExecution
    private void putLayer(String type, Color color) {
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("type", type);
        jsonLayer.put("enabled", true);
        jsonLayer.put("color", Colors.toJSONArray(color));
        AnimationLayer cosLayer = new AnimationLayer();
        cosLayer.deserialize(jsonLayer);
        animationLayers.put(type, cosLayer);
    }

    @ServerExecution
    public void applyLayer(String type, Color color) {
        putLayer(type, color);
        MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
    }

    @ServerExecution
    public void removeLayer(String type) {
        animationLayers.remove(type);
        MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
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
            layerJSON.put("type", layer.getKey());
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
            animationLayers.put((String)jsonLayer.get("type"), layer);
        }

        //load active animations
        JSONArray jsonContexts = (JSONArray)object.getOrDefault("active_animations", new JSONArray());
        for (Object o: jsonContexts) activeAnimations.add((String)o);

        //load and apply cosmetics
        if (object.containsKey("cosmetics")) {
            JSONObject cosmetics = (JSONObject)object.get("cosmetics");
            JSONArray all = (JSONArray)cosmetics.get("apply");
            for (Object o: all) {
                JSONObject cos = (JSONObject)o;
                JSONArray one_of = (JSONArray)cos.get("one_of");
                putLayer(one_of.get((int)MiscMath.random(0, one_of.size()))+"", Colors.fromJSONArray((JSONArray)cos.get("color")));
            }
        }

    }

    @ClientExecution
    public void setLocalActiveAnimations(String[] serializedList) {
        activeAnimations.clear();
        for (String anim: serializedList)
            activeAnimations.add(anim);
    }

    @Override
    public String getID() {
        return "animator";
    }
}
