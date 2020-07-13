package world.events.event;

import world.events.Event;

public class PlayerInteractEvent extends Event {

    private Integer entity, player;

    public PlayerInteractEvent(Integer entity, Integer player) {
        this.entity = entity;
        this.player = player;
    }

    public Integer getPlayer() {
        return player;
    }

    public Integer getEntity() {
        return entity;
    }

}
