package world.entities.magic.techniques.arc;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class ArcSpreadTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setArcLength(source.getBody().getArcLength() + (30 * getLevel()));
    }

    @Override
    public void update(MagicSource source) {

    }

}
