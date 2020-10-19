package world.events.event;

import world.entities.components.AnimatorComponent;
import world.events.Event;

public class EntityAnimationActivatedEvent extends Event {

    private int entityID;
    private String animationName;

    public EntityAnimationActivatedEvent(int entityID, String animationName) {
        this.entityID = entityID;
        this.animationName = animationName;
    }

    public int getEntityID() {
        return entityID;
    }

    public String getAnimationName() {
        return animationName;
    }
}
