package world.entities.magic;

import world.entities.magic.techniques.TechniqueName;

import java.util.ArrayList;

public class Spellbook {

    private ArrayList<Spell> spells;
    private ArrayList<TechniqueName> discovered_techniques;
    private int selected;

    public Spellbook() {
        this.spells = new ArrayList<>();
        this.discovered_techniques = new ArrayList<>();
    }

    public Spell getSpell(int index) { return this.spells.get(index); }
    public Spell getSelectedSpell() { return getSpell(selected); }
    public void selectSpell(int index) { this.selected = index; }
    public void addSpell(Spell spell) { this.spells.add(spell); }
    public void discoverTechnique(TechniqueName technique) { this.discovered_techniques.add(technique); }
    public boolean hasTechnique(TechniqueName technique) { return this.discovered_techniques.contains(technique); }


}
