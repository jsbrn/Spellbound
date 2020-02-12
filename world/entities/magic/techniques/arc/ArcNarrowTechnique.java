package world.entities.magic.techniques.arc;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.particles.EmissionMode;

public class ArcNarrowTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setArcLength(15);
    }

    @Override
    public void update(MagicSource source) {

    }

}
