package world.entities.components.magic.techniques.emission;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;
import world.particles.EmissionMode;

public class GravitateTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {
        source.getBody().setEmissionMode(EmissionMode.GRAVITATE);
    }

    @Override
    public void update(MagicSourceComponent source) {

    }

}
