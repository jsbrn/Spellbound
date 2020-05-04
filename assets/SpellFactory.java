package assets;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.magic.Spell;

import java.util.Random;

public class SpellFactory {

    public static String DAMAGE = "damage", HEALING = "healing", DASH = "dash";
    private static Random rng = new Random();
    private static String[] adjectives = {"Low-Quality", "Mediocre", "OK", "Normal", "Impressive", "Incredible", "Serious", "Mega"};

    public static Spell createSpell(String type, int level) {
        switch(type) {
            case "damage": return createDamageSpell(level);
            case "healing": return createHealingSpell(level);
            case "blast": return createBlastSpell(level);
        }
        return null;
    }

    public static String getAdjective(int level) {
        int base = adjectives.length / level;
        int randomized = base - 2 + rng.nextInt(5);
        int actual = (int)MiscMath.clamp(randomized, 0, adjectives.length - 1);
        return adjectives[actual];
    }

    public static Spell createDamageSpell(int level) {
        Spell dmg = new Spell();
        Color[] randomColors = new Color[]{Color.orange, Color.orange.brighter(), Color.cyan, Color.white, Color.green};
        dmg.addTechnique("movement_directional");
        dmg.addTechnique("trigger_impact");
        dmg.addTechnique("physical_collision");
        dmg.addTechnique("physical_weight");
        dmg.addTechnique("effects_decrease", level);
        dmg.addTechnique("trait_hp");
        dmg.addTechnique("physical_torque");
        dmg.addTechnique("physical_speed", rng.nextInt(level) + 2);
        dmg.addTechnique("physical_energy");
        dmg.setIconIndex(9);
        dmg.setName(getAdjective(level)+" Destruction");
        dmg.setColor(randomColors[rng.nextInt(randomColors.length)]);
        return dmg;
    }

    public static Spell createHealingSpell(int level) {
        Spell dmg = new Spell();
        Color[] randomColors = new Color[]{Color.red};
        boolean self = rng.nextInt(3) != 0;
        dmg.addTechnique(!self ? "movement_caster" : "movement_directional");
        dmg.addTechnique(!self ? "trigger_impact" : "trigger_cast");
        if (rng.nextBoolean()) dmg.addTechnique("physical_collision");
        dmg.addTechnique("effects_increase", level);
        if (self) {
            dmg.addTechnique("radius_min");
            dmg.addTechnique("arc_narrow");
            dmg.addTechnique("arc_spread", 2);
            dmg.addTechnique("rotation_spin");
            dmg.addTechnique("physical_torque", 2);
        }
        dmg.addTechnique("trait_hp");
        dmg.addTechnique("physical_speed", rng.nextInt(level) + 2);
        dmg.addTechnique("physical_energy");
        dmg.setIconIndex(9);
        dmg.setColor(randomColors[rng.nextInt(randomColors.length)]);
        dmg.setName(getAdjective(level)+" Healing");
        return dmg;
    }

    public static Spell createBlastSpell(int level) {
        Spell dmg = new Spell();
        Color[] randomColors = new Color[]{Color.white, Color.cyan};
        dmg.addTechnique("movement_directional");
        if (rng.nextBoolean()) dmg.addTechnique("physical_collision");
        dmg.addTechnique("physical_weight", level * 2);
        dmg.addTechnique("effects_increase", level);
        dmg.addTechnique("physical_speed", rng.nextInt(level));
        dmg.addTechnique("physical_energy", 3);
        dmg.setIconIndex(9);
        dmg.setColor(randomColors[rng.nextInt(randomColors.length)]);
        dmg.setName(getAdjective(level)+" "+(rng.nextBoolean() ? "Blast" : "Shockwave"));
        return dmg;
    }

}
