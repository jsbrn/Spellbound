package world.entities.ai.actions.types;

import world.entities.ai.actions.Action;
import world.entities.animations.Animation;
import world.entities.animations.AnimationLayer;

import java.util.ArrayList;

public class ChangeAnimationAction extends Action {

    private String anim, layer;
    private boolean wait, stack;

    public ChangeAnimationAction(String layer, String name, boolean waitUntilFinished, boolean stack) {
        this.anim = name;
        this.layer = layer;
        this.wait = waitUntilFinished;
        this.stack = stack;
    }

    @Override
    public void onStart() {
        Entity parent = getParent();
        ArrayList<AnimationLayer> layers = new ArrayList<>();
        layers.add(parent.getAnimationLayer(layer));
        for (AnimationLayer animationLayer: layers) {
            Animation animation = animationLayer.getAnimationByName(animationLayer.getCurrentAnimation());
            if (animation == null) continue;
            if (!animation.loops()
                    || !animationLayer.getCurrentAnimation().equals(anim))
                if (!stack) animationLayer.setBaseAnimation(anim); else animationLayer.stackAnimation(anim);
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void update() {

    }

    @Override
    public boolean finished() {
        return !wait || getParent().getAnimationLayer(layer).getAnimationByName(anim).finished();
    }

    @Override
    public void onFinish() {

    }

    public String toString() { return "SetAnimation("+layer+", "+anim+", "+wait+")[started = "+started()+", finished = "+finished()+"]"; }

}
