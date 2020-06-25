package world.events.event;

import world.magic.MagicSource;
import world.events.Event;

public class MagicImpactEvent extends Event {

    private MagicSource magicSource;
    private Entity impacted;

    public MagicImpactEvent(MagicSource source, Entity e) {
        this.magicSource = source;
        this.impacted = e;
    }

    public boolean isEntityCollision() { return getEntity() != null; }

    public MagicSource getMagicSource() {
        return magicSource;
    }

    public Entity getEntity() { return impacted; }

}
