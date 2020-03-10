package world.entities.magic.techniques.origin;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class OriginRemoteTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.getBody().setCoordinates(
                cast.getCastCoordinates()[0],
                cast.getCastCoordinates()[1]);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
