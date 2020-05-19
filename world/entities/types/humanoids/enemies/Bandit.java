package world.entities.types.humanoids.enemies;

import assets.SpellFactory;
import org.json.simple.JSONObject;
import org.newdawn.slick.Color;
import world.entities.magic.Spell;
import world.entities.states.PatrolState;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.Random;

public class Bandit extends HumanoidEntity {

    Random rng;
    private int level;

    public Bandit(int level) {
        super();
        this.level = level;
        this.setAllegiance("bandits");
        this.rng = new Random();

        getSpellbook().addSpell(SpellFactory.createSpell(SpellFactory.DAMAGE, level));

        getAnimationLayer("shirt").setBaseAnimation("dirty");
        getAnimationLayer("torso").setColor(Color.orange.darker());
        getAnimationLayer("shirt").setColor(Color.white);
        getAnimationLayer("arms").setColor(Color.orange.darker());
        getAnimationLayer("hair").setColor(HAIR_COLORS[HAIR_COLORS.length - 1 - rng.nextInt(2)]);

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = super.serialize();
        serialized.put("level", level);
        return serialized;
    }

    @Override
    public void update() {
        super.update();
        if (getCurrentState() == null) enterState(new PatrolState(7));
    }

}
