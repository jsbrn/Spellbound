package world.entities.magic.techniques;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.arc.ArcNarrowTechnique;
import world.entities.magic.techniques.arc.ArcSpreadTechnique;
import world.entities.magic.techniques.emission.GravitateTechnique;
import world.entities.magic.techniques.emission.RadiateTechnique;
import world.entities.magic.techniques.modifiers.SpeedModifierTechnique;
import world.entities.magic.techniques.modifiers.TorqueModifierTechnique;
import world.entities.magic.techniques.movement.FollowTechnique;
import world.entities.magic.techniques.movement.PropelTechnique;
import world.entities.magic.techniques.radius.*;
import world.entities.magic.techniques.rotation.*;
import world.entities.magic.techniques.triggers.CastTriggerTechnique;


public abstract class Technique {

    private int level = 1;

    public int getLevel() { return level; }
    public void setLevel(int new_level) { level = new_level; }

    public abstract void applyTo(MagicSource cast);
    public abstract void update(MagicSource cast);

    public static Technique createFrom(String name) {
        switch(name) {
            case "trigger_caster": return new CastTriggerTechnique();
            case "movement_directional": return new PropelTechnique();
            case "movement_follow": return new FollowTechnique();
            case "rotation_directional": return new RotateDirectionalTechnique();
            case "rotation_aim": return new AimTechnique();
            case "rotation_spin": return new SpinTechnique();
            case "rotation_counterspin": return new CounterSpinTechnique();
            case "rotation_caster": return new RotateCasterTechnique();
            case "rotation_target": return new TrackTechnique();
            case "arc_narrow": return new ArcNarrowTechnique();
            case "arc_spread": return new ArcSpreadTechnique();
            case "radius_min": return new RadiusMinTechnique();
            case "radius_max": return new RadiusMaxTechnique();
            case "radius_reach": return new RadiusReachTechnique();
            case "radius_expand": return new RadiusExpandTechnique();
            case "radius_shrink": return new RadiusShrinkTechnique();
            case "emission_gravitate": return new GravitateTechnique();
            case "emission_radiate": return new RadiateTechnique();
            case "modifier_torque": return new TorqueModifierTechnique();
            case "modifier_speed": return new SpeedModifierTechnique();
            default: return null;
        }
    }

}
