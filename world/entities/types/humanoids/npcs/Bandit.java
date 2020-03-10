package world.entities.types.humanoids.npcs;

import org.newdawn.slick.Color;
import world.World;
import world.entities.magic.Spell;
import world.entities.states.PatrolState;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.Random;

public class Bandit extends HumanoidEntity {

    Random rng;

    public Bandit() {
        super();
        rng = new Random();
        Color shirt = Color.orange.darker();
        getAnimationLayer("torso").setColor(shirt);
        getAnimationLayer("arms").setColor(shirt.darker());
        getAnimationLayer("head").setColor(new Color(SKIN_COLORS[rng.nextInt(SKIN_COLORS.length)]));

        Spell testSpell = new Spell();
        testSpell.addTechnique("target_point");
        testSpell.addTechnique("move_directional");
        testSpell.addTechnique("emission_radiate");
        testSpell.addTechnique("origin_caster");
        this.getSpellbook().addSpell(testSpell);

    }

    @Override
    public void update() {
        super.update();
        if (getCurrentState() == null) enterState(new PatrolState(World.getLocalPlayer(), 4));
    }

}
