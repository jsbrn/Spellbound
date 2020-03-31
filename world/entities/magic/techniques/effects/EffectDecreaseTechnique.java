package world.entities.magic.techniques.effects;

import misc.MiscMath;
import world.entities.Entity;
import world.entities.magic.MagicSource;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.List;

public class EffectDecreaseTechnique extends EffectTechnique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {

    }


    @Override
    public void affectOnce(MagicSource cast, HumanoidEntity e) {
        if (cast.hasTechnique("trait_hp")) e.addHP(-getLevel() * 2);
        if (cast.hasTechnique("trait_mana")) e.addMana(-getLevel() * 2);
    }

    @Override
    public void affectContinuous(MagicSource cast, HumanoidEntity e) {
        e.addHP(MiscMath.getConstant(-getLevel(), 1));
    }
}
