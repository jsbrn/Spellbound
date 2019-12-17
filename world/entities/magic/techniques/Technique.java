package world.entities.magic.techniques;

import world.entities.magic.MagicSource;
import world.particles.ParticleSource;

public abstract class Technique {

    private int level = 1;

    public int getLevel() { return level; }
    public void setLevel(int new_level) { level = new_level; }

    public abstract void applyTo(MagicSource cast);
    public abstract void update(MagicSource cast);

    public static Technique create(TechniqueName name) {
        if (name == TechniqueName.PROPEL) return new PropelTechnique();
        return null;
    }

}
