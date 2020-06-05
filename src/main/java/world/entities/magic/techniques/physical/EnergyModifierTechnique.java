package world.entities.magic.techniques.physical;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.Technique;

public class EnergyModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.setEnergy(getLevel() * 2);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
