package world.events.event;

import world.entities.magic.MagicSource;
import world.entities.magic.Spell;
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
