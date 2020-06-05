package assets;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.magic.Spell;
import world.entities.magic.Spellbook;
import world.entities.magic.techniques.Techniques;

import java.util.Arrays;
import java.util.Random;

public class SpellFactory {

    public static String DAMAGE = "damage", HEALING = "healing", BLAST = "blast", BARRIER = "barrier";
    private static Random rng = new Random();

    public static Spell createSpell(String type, int level) {
        switch(type) {
            case "damage": return createDamageSpell(level);
            case "healing": return createHealingSpell(level);
            case "blast": return createBlastSpell(level);
            case "barrier": return createBarrierSpell(level);
        }
        return null;
    }

    public static String discoverRandomTechnique(Spellbook spellbook, float maxRarity) {
        String[] techniques = Techniques.getAll();
        return Arrays.asList(techniques).stream()
                .sorted((t1, t2) -> rng.nextInt(techniques.length))
                .filter(t -> !spellbook.hasTechnique(t) && Techniques.getRarity(t) <= maxRarity)
                .findFirst().orElse(null);
    }

    public static Spell createDamageSpell(int level) {
        Spell dmg = new Spell();
        Color[] randomColors = new Color[]{Color.orange, Color.orange.brighter(), Color.cyan, Color.white, Color.green};
        dmg.deserialize(Assets.json("definitions/spells/damage1.json", true));
        dmg.setLevel("effects_decrease", level);
        dmg.setColor(randomColors[rng.nextInt(randomColors.length)]);
        return dmg;
    }

    public static Spell createHealingSpell(int level) {
        Spell dmg = new Spell();
        Color[] randomColors = new Color[]{Color.red};
        dmg.deserialize(Assets.json("definitions/spells/healing1.json", true));
        dmg.setLevel("effects_increase", level);
        dmg.setColor(randomColors[rng.nextInt(randomColors.length)]);
        return dmg;
    }

    public static Spell createBlastSpell(int level) {
        Spell dmg = new Spell();
        Color[] randomColors = new Color[]{Color.white, Color.cyan};
        dmg.deserialize(Assets.json("definitions/spells/blast"+(int)MiscMath.clamp(level, 1, 2)+".json", true));
        dmg.setLevel("effects_decrease", level);
        dmg.setColor(randomColors[rng.nextInt(randomColors.length)]);
        return dmg;
    }

    public static Spell createBarrierSpell(int level) {
        Spell dmg = new Spell();
        Color[] randomColors = new Color[]{Color.white, Color.cyan};
        dmg.deserialize(Assets.json("definitions/spells/barrier"+(int)MiscMath.clamp(level, 1, 2)+".json", true));
        dmg.setColor(randomColors[rng.nextInt(randomColors.length)]);
        return dmg;
    }

}
