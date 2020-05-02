package assets;

import world.entities.magic.Spell;

import java.util.Random;

public class SpellFactory {

    public static String DAMAGE = "damage", HEALING = "healing", DASH = "dash";
    private static Random rng = new Random();

    public static Spell createSpell(String type, int level) {
        switch(type) {
            case "damage": return createDamageSpell(level);
            case "healing": return null;
            case "dash": return null;
        }
        return null;
    }

    public static Spell createDamageSpell(int level) {
        Spell dmg = new Spell();
        dmg.addTechnique("movement_directional");
        dmg.addTechnique("trigger_impact");
        dmg.addTechnique("physical_weight");
        dmg.addTechnique("effects_decrease", level);
        dmg.addTechnique("trait_hp");
        dmg.addTechnique("physical_speed", rng.nextInt(level) + 2);
        dmg.addTechnique("physical_energy");
        dmg.setIconIndex(9);
        return dmg;
    }

}
