package world.magic.techniques.rotation;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class RotateCasterTechnique extends Technique {


    @Override
    public void applyTo(MagicSource source) {

    }

    @Override
    public void update(MagicSource source) {
        source.setDirection(source.getBody().getLocation().angleBetween(source.getCaster().getLocation()));
    }

}
