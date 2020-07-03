package events.event;

import events.Event;
import world.magic.MagicSource;
import world.magic.Spell;

public class SpellCastEvent extends Event {

    private Spell spell;
    private MagicSource magicSource;

    public SpellCastEvent(Spell spell, MagicSource source) {
        this.spell = spell;
        this.magicSource = source;
    }

    public Spell getSpell() {
        return spell;
    }

    public MagicSource getMagicSource() {
        return magicSource;
    }
}
