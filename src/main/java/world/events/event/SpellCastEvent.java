package world.events.event;

import world.events.Event;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.Spell;

public class SpellCastEvent extends Event {

    private Spell spell;
    private MagicSourceComponent magicSource;

    public SpellCastEvent(Spell spell, MagicSourceComponent source) {
        this.spell = spell;
        this.magicSource = source;
    }

    public Spell getSpell() {
        return spell;
    }

    public MagicSourceComponent getMagicSource() {
        return magicSource;
    }
}
