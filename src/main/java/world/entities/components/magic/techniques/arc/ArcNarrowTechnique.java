package world.entities.components.magic.techniques.arc;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class ArcNarrowTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {
        source.getBody().setArcLength(45);
    }

    @Override
    public void update(MagicSourceComponent source) {

    }

}
