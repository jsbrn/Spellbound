package events.event;

import events.Event;

public class HumanoidDeathEvent extends Event {

    private Integer humanoid;

    public HumanoidDeathEvent(Integer humanoidEntity) {
        this.humanoid = humanoidEntity;
    }

    public Integer getHumanoid() {
        return humanoid;
    }

}
