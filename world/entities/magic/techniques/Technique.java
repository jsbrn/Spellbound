package world.entities.magic.techniques;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.arc.ArcNarrowTechnique;
import world.entities.magic.techniques.arc.ArcSpreadTechnique;
import world.entities.magic.techniques.emission.GravitateTechnique;
import world.entities.magic.techniques.emission.RadiateTechnique;
import world.entities.magic.techniques.movement.FollowTechnique;
import world.entities.magic.techniques.movement.PropelTechnique;
import world.entities.magic.techniques.radius.*;
import world.entities.magic.techniques.rotation.AimTechnique;
import world.entities.magic.techniques.rotation.CounterSpinTechnique;
import world.entities.magic.techniques.rotation.SpinTechnique;
import world.entities.magic.techniques.triggers.CasterTriggerTechnique;


public abstract class Technique {

    private int level = 1;

    public int getLevel() { return level; }
    public void setLevel(int new_level) { level = new_level; }

    public abstract void applyTo(MagicSource cast);
    public abstract void update(MagicSource cast);

    public static Technique createFrom(String name) {
        switch(name) {
            case "trigger_caster": return new CasterTriggerTechnique();
            case "movement_directional": return new PropelTechnique();
            case "movement_follow": return new FollowTechnique();
            case "rotation_aim": return new AimTechnique();
            case "rotate_spin": return new SpinTechnique();
            case "rotate_counterspin": return new CounterSpinTechnique();
            case "arc_narrow": return new ArcNarrowTechnique();
            case "arc_spread": return new ArcSpreadTechnique();
            case "radius_min": return new RadiusMinTechnique();
            case "radius_max": return new RadiusMaxTechnique();
            case "radius_reach": return new RadiusReachTechnique();
            case "radius_expand": return new RadiusExpandTechnique();
            case "radius_shrink": return new RadiusShrinkTechnique();
            case "emission_gravitate": return new GravitateTechnique();
            case "emission_radiate": return new RadiateTechnique();
            default: return null;
        }
    }

}
