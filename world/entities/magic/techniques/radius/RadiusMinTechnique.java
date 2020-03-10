package world.entities.magic.techniques.radius;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class RadiusMinTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setMinRadius(getLevel());
    }

    @Override
    public void update(MagicSource source) {

    }

}
