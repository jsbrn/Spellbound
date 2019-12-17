package world.entities.magic.techniques.emission;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.particles.EmissionMode;

public class RadiateTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setEmissionMode(EmissionMode.RADIATE);
    }

    @Override
    public void update(MagicSource source) {

    }

}
