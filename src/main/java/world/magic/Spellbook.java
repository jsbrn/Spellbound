package world.magic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.magic.techniques.Techniques;
import world.entities.types.humanoids.HumanoidEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Spellbook {

    private ArrayList<Spell> spells;
    private ArrayList<String> discovered_techniques;
    private int selected;
    private HumanoidEntity parent;

    public Spellbook(HumanoidEntity parent) {
        this.spells = new ArrayList<>();
        this.discovered_techniques = new ArrayList<>();
        discoverTechnique("");
        this.parent = parent;
        for (String t: Techniques.getAll()) if (Techniques.isDefault(t)) discoverTechnique(t);
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
    public void discoverAllTechniques() { discovered_techniques.clear(); for (String t: Techniques.getAll()) discoverTechnique(t); }
    public boolean hasTechnique(String technique) { return technique != null && this.discovered_techniques.contains(technique); }

    public void setParent(HumanoidEntity parent) { this.parent = parent; }
    public HumanoidEntity getParent() { return parent; }

    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        JSONArray jsonTechniques = new JSONArray();
        jsonTechniques.addAll(discovered_techniques);
        serialized.put("discovered_techniques", jsonTechniques);
        JSONArray jsonSpells = new JSONArray();
        jsonSpells.addAll(spells.stream().map(spell -> spell.serialize()).collect(Collectors.toList()));
        serialized.put("spells", jsonSpells);
        return serialized;
    }

    public void deserialize(JSONObject json) {
        spells.clear();
        discovered_techniques.clear();
        JSONArray discovered = (JSONArray)json.get("discovered_techniques");
        discovered.forEach(t -> discoverTechnique((String)t));
        JSONArray jsonSpells = (JSONArray)json.get("spells");
        jsonSpells.forEach(js -> {
            Spell spell = new Spell();
            spell.deserialize((JSONObject)js);
            addSpell(spell);
        });
    }

}
