package world.entities.animations;

import org.newdawn.slick.Color;

import java.util.HashMap;
import java.util.Stack;

public class AnimationLayer {

    private HashMap<String, Animation> animations;
    String baseAnimation;
    private Stack<String> animationStack;

    public AnimationLayer() {
        this.animations = new HashMap<>();
        this.animationStack = new Stack<>();
        this.baseAnimation = "default";
    }

    public void setBaseAnimation(String name) {
        baseAnimation = name;
        if (baseAnimation == null) setBaseAnimation("default");
        Animation current = getAnimationByName(baseAnimation);
        if (current != null && !current.loops()) current.reset();
    }

    public void stackAnimation(String name) {
        animationStack.push(name);
        getAnimationByName(name).reset();
    }

    public void addAnimation(String name, Animation a) {
        this.animations.put(name, a);
    }

    public Animation getAnimationByName(String name) {
        Animation get = this.animations.get(name);
        return get;
    }

    public void setColor(Color color) {
        for (Animation anim: animations.values()) anim.setColor(color);
    }

    public String getCurrentAnimation() {
        if (animationStack.isEmpty()) return baseAnimation;
        String top = animationStack.peek();
        if (getAnimationByName(top).loopCount() > 0) animationStack.pop();
        return animationStack.isEmpty() ? baseAnimation : animationStack.peek();
    }

}
