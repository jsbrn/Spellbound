package world.events.event;

import world.events.Event;
import world.magic.MagicSource;

public class MagicDepletedEvent extends Event {

    private MagicSource magicSource;

    public MagicDepletedEvent(MagicSource source) {
        this.magicSource = source;
    }

    public MagicSource getMagicSource() {
        return magicSource;
    }
}
