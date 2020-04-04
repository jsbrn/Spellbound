package world.entities.magic.techniques.effects;

import misc.MiscMath;
import world.entities.magic.MagicSource;
import world.entities.types.humanoids.HumanoidEntity;

public class EffectAbsorbTechnique extends EffectTechnique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {

    }


    @Override
    public void affectOnce(MagicSource cast, HumanoidEntity e) {
        if (cast.getCaster() instanceof HumanoidEntity) return;
        HumanoidEntity caster = (HumanoidEntity)cast.getCaster();
        if (cast.hasTechnique("trait_hp") && e.getHP() > 0) {
            e.addHP(-getLevel() * 2);
            caster.addHP(getLevel() * 2);
        }
        if (cast.hasTechnique("trait_mana") && e.getMana() > 0) {
            e.addMana(-getLevel() * 2);
            e.addMana(getLevel() * 2);
        }
    }

    @Override
    public void affectContinuous(MagicSource cast, HumanoidEntity e) {
        if (cast.getCaster() instanceof HumanoidEntity) return;
        HumanoidEntity caster = (HumanoidEntity)cast.getCaster();
        double amount = MiscMath.getConstant(getLevel(), 1);
        if (cast.hasTechnique("trait_hp") && e.getHP() > amount) {
            e.addHP(-amount);
            caster.addHP(amount);
        }
        if (cast.hasTechnique("trait_mana") && e.getMana() > amount) {
            e.addMana(-amount);
            caster.addMana(amount);
        }
    }
}
