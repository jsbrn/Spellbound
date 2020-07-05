package world.entities.components.magic.techniques.physical;

import world.entities.components.magic.MagicSourceComponent;
import world.entities.components.magic.techniques.Technique;

public class EnergyModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSourceComponent cast) {
        cast.setEnergy(getLevel() * 2);
    }

    @Override
    public void update(MagicSourceComponent cast) {

    }

}
