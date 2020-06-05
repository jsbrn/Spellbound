package world.entities.magic.techniques.rotation;

import misc.Location;
import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class RotateCasterTechnique extends Technique {


    @Override
    public void applyTo(MagicSource source) {

    }

    @Override
    public void update(MagicSource source) {
        source.setDirection(source.getBody().getLocation().angleBetween(source.getCaster().getLocation()));
    }

}
