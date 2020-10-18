package world.entities.components.animations;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import misc.Colors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class AnimationLayer {

    private LinkedHashMap<String, Animation> animationsByPrioritySort;
    private boolean enabled;
    private Color color;
    private String type;

    public AnimationLayer() {
        this.animationsByPrioritySort = new LinkedHashMap<>();
        this.enabled = true;
        this.color = Color.white;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public Animation getAnimation(String context) {
        return animationsByPrioritySort.get(context);
    }

    public Animation getCurrentAnimation(ArrayList<String> activeAnimations) {
        for (Map.Entry<String, Animation> anim: animationsByPrioritySort.entrySet()) {
            if (activeAnimations.contains(anim.getKey())) return anim.getValue();
        }
        return null;
    }

    public JSONObject serialize() {
        JSONObject json = new JSONObject();
        json.put("enabled", enabled);
        json.put("type", type);

        json.put("color", Colors.toJSONArray(color));

        return json;
    }

    public void deserialize(JSONObject json) {
        this.enabled = (boolean)json.getOrDefault("enabled", true);

        System.out.println(json.toJSONString());

        JSONArray jsonColor = (JSONArray)json.getOrDefault("color", new JSONArray());
        color = Colors.fromJSONArray(jsonColor);

        //load animations for this layer from resources folder
        JSONArray jsonAnimations = (JSONArray)Assets.json("animations/"+(String)json.get("type")+"/animations.json", true);
        for (Object o: jsonAnimations) {
            JSONObject jsonAnim = (JSONObject)o;
            jsonAnim.put("type", json.get("type"));
            Animation a = Animation.deserialize(jsonAnim);
            animationsByPrioritySort.put((String)jsonAnim.get("name"), a);
        }

    }

}
