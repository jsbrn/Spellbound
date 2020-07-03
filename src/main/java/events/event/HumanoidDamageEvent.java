package events.event;

import events.Event;

public class HumanoidDamageEvent extends Event {

    private Integer humanoid;
    private int amount;

    public HumanoidDamageEvent(Integer humanoidEntity, int amount) {
        this.amount = amount;
        this.humanoid = humanoidEntity;
    }

    public Integer getHumanoid() {
        return humanoid;
    }

    public int getAmount() { return amount; }

}
