package world.events.event;

import world.entities.components.AnimatorComponent;
import world.events.Event;

public class EntityChangedAnimationEvent extends Event {

    private AnimatorComponent animatorComponent;

    public EntityChangedAnimationEvent(AnimatorComponent ac) {
        this.animatorComponent = ac;
    }

    public AnimatorComponent getAnimatorComponent() {
        return animatorComponent;
    }
}
