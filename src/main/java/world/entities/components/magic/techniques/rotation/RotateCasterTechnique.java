package world.entities.components.magic.techniques.rotation;

import world.entities.Entities;
import world.entities.components.LocationComponent;
import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class RotateCasterTechnique extends Technique {


    @Override
    public void applyTo(MagicSourceComponent source) {

    }

    @Override
    public void update(MagicSourceComponent source) {
        source.setDirection(source.getBody().getLocation().angleBetween(
                ((LocationComponent) Entities.getComponent(LocationComponent.class, source.getCaster())).getLocation()));
    }

}
