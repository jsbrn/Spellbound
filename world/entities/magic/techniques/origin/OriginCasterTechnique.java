package world.entities.magic.techniques.origin;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class OriginCasterTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.getBody().setCoordinates(
                cast.getCaster().getLocation().getCoordinates()[0],
                cast.getCaster().getLocation().getCoordinates()[1] - 0.5);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
