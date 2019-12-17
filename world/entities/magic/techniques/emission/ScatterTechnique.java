package world.entities.magic.techniques.emission;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;
import world.particles.EmissionMode;

public class ScatterTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setEmissionMode(EmissionMode.SCATTER);
    }

    @Override
    public void update(MagicSource source) {

    }

}
