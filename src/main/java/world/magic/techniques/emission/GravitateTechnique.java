package world.magic.techniques.emission;

import world.magic.MagicSource;
import world.magic.techniques.Technique;
import world.particles.EmissionMode;

public class GravitateTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setEmissionMode(EmissionMode.GRAVITATE);
    }

    @Override
    public void update(MagicSource source) {

    }

}
