package world.events.event;

import world.events.Event;
import world.entities.components.magic.MagicSourceComponent;

public class MagicDepletedEvent extends Event {

    private MagicSourceComponent magicSource;

    public MagicDepletedEvent(MagicSourceComponent source) {
        this.magicSource = source;
    }

    public MagicSourceComponent getMagicSource() {
        return magicSource;
    }
}
