package world.entities.components.magic.techniques.arc;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class ArcSpreadTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent source) {
        source.getBody().setArcLength(source.getBody().getArcLength() + (30 * getLevel()));
    }

    @Override
    public void update(MagicSourceComponent source) {

    }

}
