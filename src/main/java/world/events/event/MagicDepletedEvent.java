package world.events.event;

import world.magic.MagicSource;
import world.events.Event;

public class MagicDepletedEvent extends Event {

    private MagicSource magicSource;

    public MagicDepletedEvent(MagicSource source) {
        this.magicSource = source;
    }

    public MagicSource getMagicSource() {
        return magicSource;
    }
}
