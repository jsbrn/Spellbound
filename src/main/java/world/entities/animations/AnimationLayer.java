package world.entities.animations;

import com.github.mathiewz.slick.Color;

import java.util.HashMap;
import java.util.Stack;

public class AnimationLayer {

    private HashMap<String, Animation> animations;
    String baseAnimation;
    private Stack<String> animationStack;
    private boolean enabled;
    private Color color;

    public AnimationLayer() {
        this.animations = new HashMap<>();
        this.animationStack = new Stack<>();
        this.baseAnimation = "default";
        this.enabled = true;
        this.color = Color.white;
    }

    public void setBaseAnimation(String name) {
        baseAnimation = name;
        if (baseAnimation == null) setBaseAnimation("default");
        Animation current = getAnimationByName(baseAnimation);
        if (current != null && !current.loops()) current.reset();
    }

    public String getBaseAnimation() {
        return baseAnimation;
    }

    public void stackAnimation(String name) {
        animationStack.push(name);
        getAnimationByName(name).reset();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addAnimation(String name, Animation a) {
        this.animations.put(name, a);
    }

    public Animation getAnimationByName(String name) {
        Animation get = this.animations.get(name);
        return get;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getCurrentAnimation() {
        if (animationStack.isEmpty()) return baseAnimation;
        String top = animationStack.peek();
        if (getAnimationByName(top).loopCount() > 0) animationStack.pop();
        return animationStack.isEmpty() ? baseAnimation : animationStack.peek();
    }

}
