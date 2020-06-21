package world.magic.techniques.movement;

import world.magic.MagicSource;

public class HoverTechnique extends PropelTechnique {

    @Override
    public void applyTo(MagicSource source) {
        super.applyTo(source);
    }

    @Override
    public void update(MagicSource source) {
        source.setMoveTarget(
                source.getCastCoordinates()[0],
                source.getCastCoordinates()[1]);
    }

}
