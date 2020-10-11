package world.entities.components.animations;

import com.github.mathiewz.slick.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class AnimationLayer {

    private ArrayList<String> activeContexts;
    private LinkedHashMap<String, Animation> animationsByPrioritySort;
    private boolean enabled;
    private Color color;

    public AnimationLayer() {
        this.activeContexts = new ArrayList<>();
        this.animationsByPrioritySort = new LinkedHashMap<>();
        this.enabled = true;
        this.color = Color.white;
    }

    public void addContext(String context) {
        activeContexts.add(context);
    }

    public void removeContext(String context) {
        activeContexts.remove(context);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Animation getAnimationByName(String name) {
        Animation get = this.animationsByPrioritySort.get(name);
        return get;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Animation getCurrentAnimation() {
        for (Map.Entry<String, Animation> anim: animationsByPrioritySort.entrySet()) {
            if (activeContexts.contains(anim.getKey())) return anim.getValue();
        }
        return null;
    }

    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("enabled", enabled);
        //json.put("color", color.)
        JSONArray serializedAnims = new JSONArray();
        for (Map.Entry<String, Animation> anim: animationsByPrioritySort.entrySet()) {
            JSONObject animJSON = anim.getValue().serialize();
            animJSON.put("type", anim.getKey());
            serializedAnims.add(anim.getValue().serialize());
        }
        json.put("animations", serializedAnims);
        JSONArray jsonActiveContexts = new JSONArray();
        for (String s: activeContexts) jsonActiveContexts.add(s);
        json.put("active_contexts", jsonActiveContexts);
        return json;
    }

    public void deserialize(JSONObject json) {
        this.enabled = (boolean)json.getOrDefault("enabled", true);

        JSONArray jsonContexts = (JSONArray)json.getOrDefault("active_contexts", new JSONArray());
        for (Object o: jsonContexts) activeContexts.add((String)o);

        JSONArray jsonAnimations = (JSONArray)json.get("animations");
        for (Object o: jsonAnimations) {
            JSONObject jsonAnim = (JSONObject)o;
            Animation a = Animation.deserialize(jsonAnim);
            animationsByPrioritySort.put((String)jsonAnim.get("type"), a);
        }
    }

}
