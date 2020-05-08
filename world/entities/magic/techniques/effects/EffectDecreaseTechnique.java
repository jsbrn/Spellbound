package world.entities.magic.techniques.effects;

import misc.MiscMath;
import world.entities.actions.types.KnockbackAction;
import world.entities.magic.MagicSource;
import world.entities.types.humanoids.HumanoidEntity;

public class EffectDecreaseTechnique extends EffectTechnique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {

    }


    @Override
    public void affectOnce(MagicSource cast, HumanoidEntity e) {
        if (cast.hasTechnique("trait_hp")) e.addHP(-getLevel() * MiscMath.random(3, 8), true);
        if (cast.hasTechnique("trait_mana")) e.addMana(-getLevel() * MiscMath.random(3, 8));
        if (cast.hasTechnique("trait_x") || cast.hasTechnique("trait_y")) {
            e.clearAllActions();
            double angle = cast.hasTechnique("trait_x") && cast.hasTechnique("trait_y") ? 225 : (cast.hasTechnique("trait_x") ? 270 : 180);
            e.getActionQueue().queueAction(new KnockbackAction(2*getLevel(), angle));
        }
    }

    @Override
    public void affectContinuous(MagicSource cast, HumanoidEntity e) {
        e.addHP(MiscMath.getConstant(-getLevel() * 2, 1), true);
    }
}
