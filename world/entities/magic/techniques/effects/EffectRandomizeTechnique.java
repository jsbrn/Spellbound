package world.entities.magic.techniques.effects;

import misc.MiscMath;
import world.entities.actions.types.KnockbackAction;
import world.entities.magic.MagicSource;
import world.entities.types.humanoids.HumanoidEntity;

public class EffectRandomizeTechnique extends EffectTechnique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {

    }


    @Override
    public void affectOnce(MagicSource cast, HumanoidEntity e) {
        if (cast.hasTechnique("trait_hp")) e.setHP(e.getHP() + MiscMath.random(-getLevel() * 4, getLevel() * 4));
        if (cast.hasTechnique("trait_mana")) e.setMana(e.getMana() + MiscMath.random(-getLevel() * 4, getLevel() * 4));
        if (cast.hasTechnique("trait_x") && cast.hasTechnique("trait_y")) {
            e.clearAllActions();
            e.getActionQueue().queueAction(new KnockbackAction(getLevel(), MiscMath.random(0, 360)));
        } else if (cast.hasTechnique("trait_x")) {
            e.getActionQueue().clearActions();
            e.getActionQueue().queueAction(new KnockbackAction(getLevel(), MiscMath.random(0, 10) > 5 ? 180 : 0));
        } else if (cast.hasTechnique("trait_y")) {
            e.getActionQueue().clearActions();
            e.getActionQueue().queueAction(new KnockbackAction(getLevel(), MiscMath.random(0, 10) > 5 ? 90 : 270));
        }
    }

    @Override
    public void affectContinuous(MagicSource cast, HumanoidEntity e) {

    }
}
