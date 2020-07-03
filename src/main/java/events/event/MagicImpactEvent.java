package events.event;

import events.Event;
import world.magic.MagicSource;

public class MagicImpactEvent extends Event {

    private MagicSource magicSource;
    private Integer impacted;

    public MagicImpactEvent(MagicSource source, Integer e) {
        this.magicSource = source;
        this.impacted = e;
    }

    public boolean isEntityCollision() { return getEntity() != null; }

    public MagicSource getMagicSource() {
        return magicSource;
    }

    public Integer getEntity() { return impacted; }

}
