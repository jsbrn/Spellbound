package world.entities.animations;

import java.util.HashMap;

public class AnimationLayer {

    private HashMap<String, Animation> animations;
    String currentAnimation;

    public AnimationLayer() {
        this.animations = new HashMap<>();
        this.currentAnimation = "default";
    }

    public void setAnimation(String name) {
        currentAnimation = name;
        if (currentAnimation == null) setAnimation("default");
        Animation current = getAnimationByName(currentAnimation);
        if (current != null && !current.loops()) current.reset();
    }

    public void addAnimation(String name, Animation a) {
        this.animations.put(name, a);
    }

    public Animation getAnimationByName(String name) {
        Animation get = this.animations.get(name);
        return get;
    }
    public String getCurrentAnimation() {
        return currentAnimation;
    }
    public String getDefaultAnimation() { return currentAnimation; }

}
