package world.entities.types;

import world.entities.Entity;
import world.entities.animations.Animation;
import world.entities.magic.Spell;
import world.entities.magic.Spellbook;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.TechniqueName;

public class Player extends Entity {

    private Spellbook spellbook;

    public Player() {

        super();
        this.addAnimation("idle", new Animation("player_idle.png", 2, 1));
        this.addAnimation("walking", new Animation("player_walking.png", 4, 3));
        this.addAnimation("casting", new Animation("player_casting.png", 5, 2));
        this.setAnimation("idle");

        this.spellbook = new Spellbook();

        Spell testSpell = new Spell();
        testSpell.addTechnique(Technique.create(TechniqueName.PROPEL));
        this.spellbook.addSpell(testSpell);

    }

    public Spellbook getSpellbook() { return this.spellbook; }

}
