package world.magic.techniques.arc;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class ArcNarrowTechnique extends Technique {

    @Override
    public void applyTo(MagicSource source) {
        source.getBody().setArcLength(45);
    }

    @Override
    public void update(MagicSource source) {

    }

}
