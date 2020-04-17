package world.events.event;

import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;

public class HumanoidRespawnEvent extends Event {

    private HumanoidEntity humanoid;

    public HumanoidRespawnEvent(HumanoidEntity humanoidEntity) {
        this.humanoid = humanoidEntity;
    }

    public HumanoidEntity getHumanoid() {
        return humanoid;
    }

}
