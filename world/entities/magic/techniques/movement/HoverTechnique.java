package world.entities.magic.techniques.movement;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class HoverTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {

    }

    @Override
    public void update(MagicSource source) {
        source.setMoveTarget(source.getCaster().getCoordinates()[0] + 0.5, source.getCaster().getCoordinates()[1]);
    }

}
