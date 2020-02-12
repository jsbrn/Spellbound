package world.entities.magic;

import world.entities.magic.techniques.Techniques;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;

public class Spellbook {

    private ArrayList<Spell> spells;
    private ArrayList<String> discovered_techniques;
    private int selected;
    private HumanoidEntity parent;

    public Spellbook(HumanoidEntity parent) {
        this.spells = new ArrayList<>();
        this.discovered_techniques = new ArrayList<>();
        this.parent = parent;
    }

    public void cast(double wx, double wy) {
        getSelectedSpell().cast(wx, wy, parent);
    }

    public Spell getSpell(int index) { return index >= 0 && index < spells.size() ? this.spells.get(index) : null; }
    public Spell getSelectedSpell() { return getSpell(selected); }
    public int getSelectedIndex() { return selected; }
    public void selectSpell(int index) { this.selected = index; }
    public void addSpell(Spell spell) { this.spells.add(spell); }
    public ArrayList<Spell> getSpells() { return spells; }

    public int getTechniqueCount() { return discovered_techniques.size(); }
    public void discoverTechnique(String technique) { this.discovered_techniques.add(technique); }
    public void discoverAllTechniques() { for (String t: Techniques.getAll()) discoverTechnique(t); }
    public boolean hasTechnique(String technique) { return technique != null && this.discovered_techniques.contains(technique); }

    public void setParent(HumanoidEntity parent) { this.parent = parent; }
    public HumanoidEntity getParent() { return parent; }


}
