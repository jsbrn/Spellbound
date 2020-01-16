package world.entities.types.humanoids.npcs;

import world.entities.animations.Animation;
import world.entities.magic.Spell;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.TechniqueName;

public class Bandit extends NPC {

    public Bandit() {

        super();

        this.setMaxHP(10);
        this.setMaxMana(10);
        this.setHP(10);
        this.setMana(5);
        this.setMaxStamina(10);

        Spell testSpell = new Spell();
        testSpell.addTechnique(Technique.create(TechniqueName.PROPEL));
        testSpell.addTechnique(Technique.create(TechniqueName.RADIATE));
        this.getSpellbook().addSpell(testSpell);

    }

}
