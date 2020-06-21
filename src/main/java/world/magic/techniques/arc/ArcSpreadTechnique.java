package world.magic.techniques.arc;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class ArcSpreadTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setArcLength(source.getBody().getArcLength() + (30 * getLevel()));
    }

    @Override
    public void update(MagicSource source) {

    }

}
