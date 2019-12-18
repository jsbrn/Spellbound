package world.entities;

import misc.MiscMath;
import world.entities.magic.Spellbook;

public class HumanoidEntity extends Entity {

    private Spellbook spellbook;
    private double hp, mana, stamina, max_hp, max_mana, max_stamina;

    @Override
    public void update() {
        super.update();
        addHP(MiscMath.getConstant(max_hp, 180));
        addMana(MiscMath.getConstant(max_mana, 20));
        addStamina(MiscMath.getConstant(max_stamina, 10));
    }

    public double getHP() { return hp; }
    public double getMana() { return mana; }
    public double getStamina() { return mana; }
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

    public Spellbook getSpellbook() { return spellbook; }

    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

}
