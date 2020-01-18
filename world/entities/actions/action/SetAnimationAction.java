package world.entities.actions.action;

import world.entities.Entity;
import world.entities.actions.Action;
import world.entities.animations.Animation;
import world.entities.animations.AnimationLayer;

import java.util.ArrayList;

public class SetAnimationAction extends Action {

    private String anim, layer;
    private boolean wait;

    public SetAnimationAction(String layer, String name, boolean waitUntilFinished) {
        this.anim = name;
        this.layer = layer;
        this.wait = waitUntilFinished;
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
                animationLayer.setAnimation(anim);
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

    public String toString() { return "SetAnimation("+layer+", "+anim+", "+wait+")[started = "+started()+", finished = "+finished()+"]"; }

}
