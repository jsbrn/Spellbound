package world.entities.magic.techniques;

import world.entities.magic.MagicSource;
import world.entities.magic.techniques.arc.ArcNarrowTechnique;
import world.entities.magic.techniques.arc.ArcSpreadTechnique;
import world.entities.magic.techniques.effects.EffectDecreaseTechnique;
import world.entities.magic.techniques.effects.EffectRandomizeTechnique;
import world.entities.magic.techniques.effects.TraitSelectorTechnique;
import world.entities.magic.techniques.emission.GravitateTechnique;
import world.entities.magic.techniques.emission.RadiateTechnique;
import world.entities.magic.techniques.modifiers.SpeedModifierTechnique;
import world.entities.magic.techniques.modifiers.TorqueModifierTechnique;
import world.entities.magic.techniques.movement.AuraTechnique;
import world.entities.magic.techniques.movement.FollowTechnique;
import world.entities.magic.techniques.movement.HoverTechnique;
import world.entities.magic.techniques.movement.PropelTechnique;
import world.entities.magic.techniques.radius.*;
import world.entities.magic.techniques.rotation.*;
import world.entities.magic.techniques.triggers.CastTriggerTechnique;
import world.entities.magic.techniques.triggers.DepletionTriggerTechnique;
import world.entities.magic.techniques.triggers.IntersectionTriggerTechnique;


public abstract class Technique {

    private int level = 1;

    private String id;

    public int getLevel() { return level; }

    public String getID() {
        return id;
    }

    public void setLevel(int new_level) { level = new_level; }

    public abstract void applyTo(MagicSource cast);
    public abstract void update(MagicSource cast);

    public static Technique createFrom(String name) {
        Technique creation;
        switch(name) {
            case "trigger_cast": creation = new CastTriggerTechnique(); break;
            case "trigger_depletion": creation = new DepletionTriggerTechnique(); break;
            case "trigger_collision": creation = new IntersectionTriggerTechnique(); break;
            case "movement_caster": creation = new AuraTechnique(); break;
            case "movement_directional": creation = new PropelTechnique(); break;
            case "movement_follow": creation = new FollowTechnique(); break;
            case "movement_hover": creation = new HoverTechnique(); break;
            case "rotation_directional": creation = new RotateDirectionalTechnique(); break;
            case "rotation_aim": creation = new AimTechnique(); break;
            case "rotation_spin": creation = new SpinTechnique(); break;
            case "rotation_counterspin": creation = new CounterSpinTechnique(); break;
            case "rotation_caster": creation = new RotateCasterTechnique(); break;
            case "rotation_target": creation = new TrackTechnique(); break;
            case "arc_narrow": creation = new ArcNarrowTechnique(); break;
            case "arc_spread": creation = new ArcSpreadTechnique(); break;
            case "radius_min": creation = new RadiusMinTechnique(); break;
            case "radius_max": creation = new RadiusMaxTechnique(); break;
            case "radius_reach": creation = new RadiusReachTechnique(); break;
            case "radius_expand": creation = new RadiusExpandTechnique(); break;
            case "radius_shrink": creation = new RadiusShrinkTechnique(); break;
            case "emission_gravitate": creation = new GravitateTechnique(); break;
            case "emission_radiate": creation = new RadiateTechnique(); break;
            case "physical_torque": creation = new TorqueModifierTechnique(); break;
            case "physical_speed": creation = new SpeedModifierTechnique(); break;
            case "effects_increase": creation = new EffectDecreaseTechnique(); break;
            case "effects_decrease": creation = new EffectDecreaseTechnique(); break;
            case "effects_randomize": creation = new EffectRandomizeTechnique(); break;
            case "trait_hp": creation = new TraitSelectorTechnique(); break;
            case "trait_mana": creation = new TraitSelectorTechnique(); break;
            default: creation = null;
        }
        if (creation != null) creation.id = name;
        return creation;
    }

}
