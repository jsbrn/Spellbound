package world.entities.magic.techniques.physical;

import misc.MiscMath;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

import java.util.List;

public class BarrierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {

    }

    @Override
    public void update(MagicSource cast) {
        List<MagicSource> colliding = cast.getCollidingMagic();
        colliding.stream().forEach(ms -> {
            ms.addEnergy(MiscMath.getConstant(-getLevel() * 4, 1));
            ms.addMoveSpeed(MiscMath.getConstant(-getLevel() * 2, 1));
        });
    }

}
