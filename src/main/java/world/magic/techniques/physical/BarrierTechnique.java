package world.magic.techniques.physical;

import misc.MiscMath;
import world.magic.MagicSource;
import world.magic.techniques.Technique;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.List;

public class BarrierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {
        List<MagicSource> colliding = cast.getCollidingMagic();
        colliding.stream()
            .filter(ms -> !((HumanoidEntity)ms.getCaster()).isAlliedTo(((HumanoidEntity)cast.getCaster())))
            .forEach(ms -> {
            ms.addEnergy(MiscMath.getConstant(-getLevel() * 16, 1));
            ms.addMoveSpeed(MiscMath.getConstant(-getLevel() * 4, 1));
        });
    }

}
