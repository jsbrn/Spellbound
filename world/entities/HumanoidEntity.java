package world.entities;

import world.entities.magic.Spellbook;

public class HumanoidEntity extends Entity {

    private Spellbook spellbook;
    private double hp, mana, max_hp, max_mana;

    public void setSpellbook(Spellbook spellbook) {
        this.spellbook = spellbook;
    }

    public double getHP() { return hp; }
    public double getMana() { return mana; }
    public double getMaxHP() { return max_hp; }
    public double getMaxMana() { return max_mana; }

}
