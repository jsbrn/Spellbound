package world.entities.components.magic.techniques.physical;

import misc.MiscMath;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

import java.util.List;

public class BarrierTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {

    }

    @Override
    public void update(MagicSourceComponent cast) {
        List<MagicSourceComponent> colliding = cast.getCollidingMagic();
        colliding.stream()
            .forEach(ms -> {
            ms.addEnergy(MiscMath.getConstant(-getLevel() * 16, 1));
            ms.addMoveSpeed(MiscMath.getConstant(-getLevel() * 4, 1));
        });
    }

}
