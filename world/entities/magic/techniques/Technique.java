package world.entities.magic.techniques;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.arc.ArcNarrowTechnique;
import world.entities.magic.techniques.arc.ArcSpreadTechnique;
import world.entities.magic.techniques.emission.GravitateTechnique;
import world.entities.magic.techniques.emission.RadiateTechnique;
import world.entities.magic.techniques.movement.FollowTechnique;
import world.entities.magic.techniques.movement.PropelTechnique;
import world.entities.magic.techniques.origin.OriginCasterTechnique;
import world.entities.magic.techniques.origin.OriginRemoteTechnique;
import world.entities.magic.techniques.radius.*;
import world.entities.magic.techniques.rotation.LookAtTechnique;
import world.entities.magic.techniques.rotation.CounterSpinTechnique;
import world.entities.magic.techniques.rotation.SpinTechnique;
import world.entities.magic.techniques.targeting.TargetCasterTechnique;
import world.entities.magic.techniques.targeting.TargetPointTechnique;


public abstract class Technique {

    private int level = 1;

    public int getLevel() { return level; }
    public void setLevel(int new_level) { level = new_level; }

    public abstract void applyTo(MagicSource cast);
    public abstract void update(MagicSource cast);

    public static Technique createFrom(String name) {
        switch(name) {
            case "origin_caster": return new OriginCasterTechnique();
            case "origin_mouse": return new OriginRemoteTechnique();
            case "target_caster": return new TargetCasterTechnique();
            case "target_point": return new TargetPointTechnique();
            case "move_directional": return new PropelTechnique();
            case "move_follow": return new FollowTechnique();

            case "rotate_follow": return new LookAtTechnique();
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
