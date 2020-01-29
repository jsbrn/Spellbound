package world.entities.types.humanoids;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.Entity;
import world.entities.animations.Animation;
import world.entities.magic.Spell;
import world.entities.magic.Spellbook;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.TechniqueName;

public class HumanoidEntity extends Entity {

    private Spellbook spellbook;
    private double hp, mana, stamina, max_hp, max_mana, max_stamina;
    private boolean hostile;

    private int crystals, gold, dyes;

    public static Color[] SKIN_COLORS
            = new Color[]{new Color(230, 210, 155), new Color(110, 90, 72)};

    public HumanoidEntity() {

        super();

        this.setMaxHP(10);
        this.setMaxMana(Integer.MAX_VALUE);
        this.setHP(10);
        this.setMana(Integer.MAX_VALUE);
        this.setMaxStamina(10);

        this.spellbook = new Spellbook(this);
        Spell testSpell = new Spell();
        testSpell.addTechnique(TechniqueName.PROPEL);
        testSpell.addTechnique(TechniqueName.RADIATE);
        this.getSpellbook().addSpell(testSpell);

        this.addAnimation("torso", "default", new Animation("humanoid/torso_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("legs", "default", new Animation("humanoid/legs_idle.png", 2, 1, 16, true, true, Color.orange));
        this.addAnimation("arms", "default", new Animation("humanoid/arms_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("legs", "walking", new Animation("humanoid/legs_walking.png", 2, 4, 16, true, true, Color.orange));
        this.addAnimation("arms", "walking", new Animation("humanoid/arms_walking.png", 2, 4, 16, true, true, Color.red));
        this.addAnimation("arms", "casting", new Animation("humanoid/arms_casting.png", 4, 3, 16, false, true, Color.red));
        this.addAnimation("head", "default", new Animation("humanoid/head_idle.png", 2, 1, 16, true, true, Color.white));
        this.addAnimation("head", "talking", new Animation("humanoid/head_talking.png", 3, 2, 16, true, true, Color.white));

    }

    @Override
    public void update() {
        super.update();
        addHP(MiscMath.getConstant(max_hp, 180));
        addMana(MiscMath.getConstant(max_mana, 10));
        addStamina(MiscMath.getConstant(max_stamina, 7.5));
    }

    public double getHP() { return hp; }
    public double getMana() { return mana; }
    public double getStamina() { return stamina; }
    public double getMaxHP() { return max_hp; }
    public double getMaxMana() { return max_mana; }
    public double getMaxStamina() { return max_stamina; }

    public int getCrystalCount() { return crystals; }
    public int getGoldCount() { return gold; }
    public int getDyeCount() { return dyes; }

    public void addGold(int gold) { this.gold += gold; }
    public void addCrystals(int crystals) { this.crystals += crystals; }
    public void addDyes(int dyes) { this.dyes += dyes; }

    public void setMana(double mana) { this.mana = MiscMath.clamp(mana, 0, max_mana); }
    public void addMana(double amount) { setMana(mana + amount); }
    public void setHP(double hp) { this.hp = MiscMath.clamp(hp, 0, max_hp); }
    public void addHP(double amount) { setHP(hp + amount); }
    public void setStamina(double amount) { this.stamina = MiscMath.clamp(amount, 0, max_stamina); }
    public void addStamina(double amount) { setStamina(stamina + amount); }

    public void setMaxHP(double amount) { this.max_hp = amount; }
    public void setMaxMana(double amount) { this.max_mana = amount; }
    public void setMaxStamina(double amount) { this.max_stamina = amount; }

    public boolean isHostile() { return hostile; }
    public void setHostile(boolean h) { this.hostile = h; }

    public Spellbook getSpellbook() { return spellbook; }
    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }


}
