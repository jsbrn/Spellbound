package world.magic.techniques.radius;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class RadiusMaxTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setDepthRadius(getLevel() / 2f);
    }

    @Override
    public void update(MagicSource source) {

    }

}
