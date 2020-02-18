package world.entities.magic.techniques;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.arc.ArcNarrowTechnique;
import world.entities.magic.techniques.emission.GravitateTechnique;
import world.entities.magic.techniques.emission.RadiateTechnique;
import world.entities.magic.techniques.emission.ScatterTechnique;
import world.entities.magic.techniques.movement.FollowTechnique;
import world.entities.magic.techniques.movement.PropelTechnique;
import world.entities.magic.techniques.rotation.LookAtTechnique;
import world.entities.magic.techniques.rotation.CounterSpinTechnique;
import world.entities.magic.techniques.rotation.SpinTechnique;
import world.entities.magic.techniques.targeting.GuideTechnique;


public abstract class Technique {

    private int level = 1;

    public int getLevel() { return level; }
    public void setLevel(int new_level) { level = new_level; }

    public abstract void applyTo(MagicSource cast);
    public abstract void update(MagicSource cast);

    public static Technique createFrom(String name) {
        switch(name) {
            case "target_mouse": return new GuideTechnique();
            case "move_directional": return new PropelTechnique();
            case "rotate_spin": return new CounterSpinTechnique();
            case "rotate_follow": return new LookAtTechnique();
            case "move_follow": return new FollowTechnique();
            case "emission_gravitate": return new GravitateTechnique();
            case "emission_radiate": return new RadiateTechnique();
            case "arc_narrow": return new ArcNarrowTechnique();
            default: return null;
        }
    }

}
