package world.events.event;

import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;

public class HumanoidDeathEvent extends Event {

    private HumanoidEntity humanoid;

    public HumanoidDeathEvent(HumanoidEntity humanoidEntity) {
        this.humanoid = humanoidEntity;
    }

    public HumanoidEntity getHumanoid() {
        return humanoid;
    }

}
