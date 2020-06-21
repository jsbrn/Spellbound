package world.magic.techniques.effects;

import gui.sound.SoundManager;
import misc.MiscMath;
import world.entities.ai.actions.types.KnockbackAction;
import world.magic.MagicSource;
import world.entities.types.humanoids.HumanoidEntity;

public class EffectIncreaseTechnique extends EffectTechnique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {

    }


    @Override
    public void affectOnce(MagicSource cast, HumanoidEntity e) {
        if (cast.hasTechnique("trait_hp"))
            e.addHP(getLevel() * MiscMath.random(4, 6), true);
        SoundManager.playSound(SoundManager.MAGIC_POSITIVE, 1.0f, e.getLocation());
        if (cast.hasTechnique("trait_mana")) {
            e.addMana(getLevel() * MiscMath.random(3, 8));
            SoundManager.playSound(SoundManager.MAGIC_POSITIVE, 1.0f, e.getLocation());
        }
        if (cast.hasTechnique("trait_x") || cast.hasTechnique("trait_y")) {
            e.clearAllActions();
            double angle = cast.hasTechnique("trait_x") && cast.hasTechnique("trait_y") ? 45 : (cast.hasTechnique("trait_x") ? 90 : 0);
            e.getActionQueue().queueAction(new KnockbackAction(2*getLevel(), angle));
            SoundManager.playSound(SoundManager.MAGIC_DASH, 1.0f, e.getLocation());
        }
    }

    @Override
    public void affectContinuous(MagicSource cast, HumanoidEntity e) {
        e.addHP(MiscMath.getConstant(getLevel() * 2, 1), true);
    }
}
