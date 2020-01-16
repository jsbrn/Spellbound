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

        this.addAnimation("idle", new Animation("bandit_idle.png", 2, 1));
        this.addAnimation("walking", new Animation("bandit_walking.png", 4, 3));
        this.addAnimation("casting", new Animation("bandit_casting.png", 5, 4));
        this.setAnimation("idle");

        Spell testSpell = new Spell();
        testSpell.addTechnique(Technique.create(TechniqueName.PROPEL));
        testSpell.addTechnique(Technique.create(TechniqueName.RADIATE));
        this.getSpellbook().addSpell(testSpell);

    }

}
