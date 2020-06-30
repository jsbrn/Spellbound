package world.events.event;

import world.events.Event;
import world.magic.Spell;

public class CastFailedEvent extends Event {

    private Integer caster;
    private Spell spell;

    public CastFailedEvent(Integer caster, Spell spell) {
        this.caster = caster;
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    public Integer getCaster() {
        return caster;
    }
}
