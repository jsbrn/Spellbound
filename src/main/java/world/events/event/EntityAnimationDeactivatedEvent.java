package world.events.event;

import world.events.Event;

public class EntityAnimationDeactivatedEvent extends Event {

    private int entityID;
    private String animationName;

    public EntityAnimationDeactivatedEvent(int entityID, String animationName) {
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
