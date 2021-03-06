package world.entities.magic.techniques.movement;

import misc.Location;
import world.entities.magic.MagicSource;

public class AuraTechnique extends PropelTechnique {

    @Override
    public void applyTo(MagicSource source) {
        super.applyTo(source);
    }

    @Override
    public void update(MagicSource source) {
        source.setMoveTarget(
                source.getCaster().getLocation().getCoordinates()[0],
                source.getCaster().getLocation().getCoordinates()[1]);
    }

}
