package world.events.event;

import world.events.Event;
import world.entities.components.magic.MagicSourceComponent;

public class MagicImpactEvent extends Event {

    private MagicSourceComponent magicSource;
    private Integer impacted;

    public MagicImpactEvent(MagicSourceComponent source, Integer e) {
        this.magicSource = source;
        this.impacted = e;
    }

    public boolean isEntityCollision() { return getEntity() != null; }

    public MagicSourceComponent getMagicSource() {
        return magicSource;
    }

    public Integer getEntity() { return impacted; }

}
