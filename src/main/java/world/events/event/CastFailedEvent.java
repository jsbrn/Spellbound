package world.events.event;

import world.magic.Spell;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.Event;

public class CastFailedEvent extends Event {

    private HumanoidEntity caster;
    private Spell spell;

    public CastFailedEvent(HumanoidEntity caster, Spell spell) {
        this.caster = caster;
        this.spell = spell;
    }

    public Spell getSpell() {
        return spell;
    }

    public HumanoidEntity getCaster() {
        return caster;
    }
}
