package world.entities.magic.techniques.radius;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class RadiusMaxTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setMaxRadius(getLevel() / 2f);
    }

    @Override
    public void update(MagicSource source) {

    }

}
