package world.entities.components.magic.techniques.movement;

import world.entities.components.magic.MagicSourceComponent;

public class HoverTechnique extends PropelTechnique {

    @Override
    public void applyTo(MagicSourceComponent source) {
        super.applyTo(source);
    }

    @Override
    public void update(MagicSourceComponent source) {
        source.setMoveTarget(
                source.getCastCoordinates()[0],
                source.getCastCoordinates()[1]);
    }

}
