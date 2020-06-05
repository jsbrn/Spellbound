package world.events.event;

import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;

public class HumanoidDamageEvent extends Event {

    private HumanoidEntity humanoid;
    private int amount;

    public HumanoidDamageEvent(HumanoidEntity humanoidEntity, int amount) {
        this.amount = amount;
        this.humanoid = humanoidEntity;
    }

    public HumanoidEntity getHumanoid() {
        return humanoid;
    }

    public int getAmount() { return amount; }

}
