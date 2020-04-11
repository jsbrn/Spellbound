package world.entities.types.humanoids;

import gui.states.GameScreen;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import world.entities.Entity;
import world.entities.animations.Animation;
import world.entities.magic.Spell;
import world.entities.magic.Spellbook;
import world.entities.magic.techniques.Technique;

public class HumanoidEntity extends Entity {

    public static Color[] SKIN_COLORS
            = new Color[]{new Color(230, 210, 155), new Color(110, 90, 72)};

    private Spellbook spellbook;
    private double hp, mana, stamina, max_hp, max_mana, max_stamina;

    private boolean isDead;
    private String allegiance;
    private int crystals, gold, dyes;

    public HumanoidEntity() {

        super();

        this.allegiance = "default";
        this.setMaxHP(10);
        this.setMaxMana(Integer.MAX_VALUE);
        this.hp = 10;
        this.setMana(Integer.MAX_VALUE);
        this.setMaxStamina(10);

        this.spellbook = new Spellbook(this);

        this.addAnimation("torso", "default", new Animation("humanoid/torso_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("legs", "default", new Animation("humanoid/legs_idle.png", 2, 1, 16, true, true, Color.orange));
        this.addAnimation("arms", "default", new Animation("humanoid/arms_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("legs", "walking", new Animation("humanoid/legs_walking.png", 2, 4, 16, true, true, Color.orange));
        this.addAnimation("arms", "walking", new Animation("humanoid/arms_walking.png", 2, 4, 16, true, true, Color.red));
        this.addAnimation("arms", "casting", new Animation("humanoid/arms_casting.png", 4, 3, 16, false, true, Color.red));
        this.addAnimation("arms", "falling", new Animation("humanoid/arms_falling.png", 1, 1, 16, true, true, Color.red));
        this.addAnimation("arms", "pushed", new Animation("humanoid/arms_pushed.png", 6, 3, 16, false, true, Color.red));
        this.addAnimation("legs", "falling", new Animation("humanoid/legs_falling.png", 1, 1, 16, true, true, Color.orange));
        this.addAnimation("legs", "pushed", new Animation("humanoid/legs_pushed.png", 6, 3, 16, false, true, Color.orange));
        this.addAnimation("head", "default", new Animation("humanoid/head_idle.png", 2, 1, 16, true, true, Color.white));
        this.addAnimation("head", "talking", new Animation("humanoid/head_talking.png", 3, 2, 16, true, true, Color.white));

        this.addAnimation("torso", "dying", new Animation("humanoid/torso_dying.png", 4, 3, 16, false, false, Color.red));
        this.addAnimation("torso", "dead", new Animation("humanoid/torso_dead.png", 1, 1, 16, false, false, Color.red));
        this.addAnimation("arms", "dying", new Animation("humanoid/arms_dying.png", 4, 3, 16, false, false, Color.orange));
        this.addAnimation("arms", "dead", new Animation("humanoid/arms_dead.png", 1, 1, 16, false, false, Color.orange));
        this.addAnimation("legs", "dying", new Animation("humanoid/legs_dying.png", 4, 3, 16, false, false, Color.orange));
        this.addAnimation("legs", "dead", new Animation("humanoid/legs_dead.png", 1, 1, 16, false, false, Color.orange));
        this.addAnimation("head", "dying", new Animation("humanoid/head_dying.png", 4, 3, 16, false, false, Color.orange));
        this.addAnimation("head", "dead", new Animation("humanoid/head_dead.png", 1, 1, 16, false, false, Color.orange));



    }

    @Override
    public void update() {
        if (isDead()) return;
        super.update();
        addHP(MiscMath.getConstant(max_hp, 300));
        addMana(MiscMath.getConstant(max_mana, 30));
        addStamina(MiscMath.getConstant(max_stamina, 7.5));
    }

    public String getAllegiance() { return allegiance; }
    public void setAllegiance(String allegiance) { this.allegiance = allegiance; }
    public boolean isAlliedTo(HumanoidEntity e) { return e.allegiance.equals(allegiance); }

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
    public void setHP(double amount) {
        int oldHP = (int)hp;
        this.hp = MiscMath.clamp(amount, 0, max_hp);
        if ((int)hp != oldHP) {
            int diff = (int)hp - oldHP;
            GameScreen.getGUI().floatText(
                    getLocation(),
                    diff > 0 ? "+"+diff : ""+diff,
                    diff > 0 ? Color.green : Color.red,
                    4,
                    500);
        }
        if (!isDead && hp == 0) {
            isDead = true;
            setIsTile(true);
            clearAllActions();
            getAnimationLayer("torso").stackAnimation("dying");
            getAnimationLayer("arms").stackAnimation("dying");
            getAnimationLayer("legs").stackAnimation("dying");
            getAnimationLayer("head").stackAnimation("dying");
            getAnimationLayer("torso").setBaseAnimation("dead");
            getAnimationLayer("arms").setBaseAnimation("dead");
            getAnimationLayer("legs").setBaseAnimation("dead");
            getAnimationLayer("head").setBaseAnimation("dead");
        }
    }
    public void addHP(double amount) { setHP(hp + amount); }
    public void setStamina(double amount) { this.stamina = MiscMath.clamp(amount, 0, max_stamina); }
    public void addStamina(double amount) { setStamina(stamina + amount); }

    public void setMaxHP(double amount) { this.max_hp = amount; }
    public void setMaxMana(double amount) { this.max_mana = amount; }
    public void setMaxStamina(double amount) { this.max_stamina = amount; }

    public void resurrect() {
        if (isDead) {
            setHP(max_hp);
            setMana(max_mana);
            setStamina(0); //probably out of breath if you come back from the dead.
            clearAllActions();
            setIsTile(false);
            getAnimationLayer("torso").setBaseAnimation("default");
            getAnimationLayer("arms").setBaseAnimation("default");
            getAnimationLayer("legs").setBaseAnimation("default");
            getAnimationLayer("head").setBaseAnimation("default");
            isDead = false;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public Spellbook getSpellbook() { return spellbook; }
    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }


}
