package world.magic.techniques.physical;

import world.magic.MagicSource;
import world.magic.techniques.Technique;

public class EnergyModifierTechnique extends Technique {

    @Override
    public void applyTo(MagicSource cast) {
        cast.setEnergy(getLevel() * 2);
    }

    @Override
    public void update(MagicSource cast) {

    }

}
