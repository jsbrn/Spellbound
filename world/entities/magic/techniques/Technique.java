package world.entities.magic.techniques;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.emission.GravitateTechnique;
import world.entities.magic.techniques.emission.RadiateTechnique;
import world.entities.magic.techniques.emission.ScatterTechnique;
import world.entities.magic.techniques.movement.HoverTechnique;
import world.entities.magic.techniques.movement.PropelTechnique;
import world.entities.magic.techniques.rotation.CounterSpinTechnique;
import world.entities.magic.techniques.rotation.SpinTechnique;

public abstract class Technique {

    private int level = 1;

    public int getLevel() { return level; }
    public void setLevel(int new_level) { level = new_level; }

    public abstract void applyTo(MagicSource cast);
    public abstract void update(MagicSource cast);

    public static Technique create(TechniqueName name) {
        if (name == TechniqueName.PROPEL) return new PropelTechnique();
        if (name == TechniqueName.HOVER) return new HoverTechnique();
        if (name == TechniqueName.SPIN) return new SpinTechnique();
        if (name == TechniqueName.COUNTER_SPIN) return new CounterSpinTechnique();
        if (name == TechniqueName.GRAVITATE) return new GravitateTechnique();
        if (name == TechniqueName.RADIATE) return new RadiateTechnique();
        if (name == TechniqueName.SCATTER) return new ScatterTechnique();
        if (name == TechniqueName.POINT) return new ScatterTechnique();
        return null;
    }

}
