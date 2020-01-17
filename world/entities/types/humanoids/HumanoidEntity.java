package world.entities.types.humanoids;

import misc.MiscMath;
import org.newdawn.slick.Color;
import world.entities.Entity;
import world.entities.animations.Animation;
import world.entities.magic.Spellbook;

public class HumanoidEntity extends Entity {

    private Spellbook spellbook;
    private double hp, mana, stamina, max_hp, max_mana, max_stamina;

    public HumanoidEntity() {
        super();
        this.spellbook = new Spellbook(this);
        this.addAnimation("torso", "default", new Animation("humanoid/torso_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("head", "default", new Animation("humanoid/head_idle.png", 2, 1, 16, true, true, Color.white));
        this.addAnimation("legs", "default", new Animation("humanoid/legs_idle.png", 2, 1, 16, true, true, Color.orange));
        this.addAnimation("arms", "default", new Animation("humanoid/arms_idle.png", 2, 1, 16, true, true, Color.red));
        this.addAnimation("legs", "walking", new Animation("humanoid/legs_walking.png", 2, 4, 16, true, true, Color.orange));
        this.addAnimation("arms", "walking", new Animation("humanoid/arms_walking.png", 2, 4, 16, true, true, Color.red));
        this.addAnimation("arms", "casting", new Animation("humanoid/arms_casting.png", 4, 3, 16, false, true, Color.red));
    }

    @Override
    public void update() {
        super.update();
        addHP(MiscMath.getConstant(max_hp, 180));
        addMana(MiscMath.getConstant(max_mana, 1));
        addStamina(MiscMath.getConstant(max_stamina, 7.5));
    }

    public double getHP() { return hp; }
    public double getMana() { return mana; }
    public double getStamina() { return stamina; }
    public double getMaxHP() { return max_hp; }
    public double getMaxMana() { return max_mana; }
    public double getMaxStamina() { return max_stamina; }

    public void setMana(double mana) { this.mana = MiscMath.clamp(mana, 0, max_mana); }
    public void addMana(double amount) { setMana(mana + amount); }
    public void setHP(double hp) { this.hp = MiscMath.clamp(hp, 0, max_hp); }
    public void addHP(double amount) { setHP(hp + amount); }
    public void setStamina(double amount) { this.stamina = MiscMath.clamp(amount, 0, max_stamina); }
    public void addStamina(double amount) { setStamina(stamina + amount); }

    public void setMaxHP(double amount) { this.max_hp = amount; }
    public void setMaxMana(double amount) { this.max_mana = amount; }
    public void setMaxStamina(double amount) { this.max_stamina = amount; }

    public Spellbook getSpellbook() { return spellbook; }
    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

}
