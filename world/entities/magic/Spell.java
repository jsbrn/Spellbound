package world.entities.magic;

import assets.Assets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.World;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.Techniques;
import world.events.EventDispatcher;
import world.events.event.SpellCastEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class Spell {

    private String name;
    private ArrayList<String> techniques;
    private HashMap<String, Integer> levels;

    private int iconIndex;
    private Color color;

    public Spell() {
        this.name = "Untitled Spell";
        this.techniques = new ArrayList<>();
        this.levels = new HashMap<>();
        this.color = Color.white;
    }

    public Spell(Spell template) {
        this();
        this.techniques.addAll(template.techniques);
        this.levels.putAll(template.levels);
        this.color = template.color;
        this.iconIndex = template.iconIndex;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void addTechnique(String technique) { this.techniques.add(technique); }
    public void removeTechnique(String technique) {
        this.techniques.remove(technique);
        this.levels.remove(technique);
    }
    public boolean hasTechnique(String techniqueName) { return techniques.contains(techniqueName); }

    private ArrayList<Technique> loadTechniques() {
        ArrayList<Technique> loaded = new ArrayList<>();
        String[] allTechs = Techniques.getAll();
        //maintain proper order of techniques
        for (String techniqueName: allTechs) {
            if (hasTechnique(techniqueName)) {
                Technique instance = Technique.createFrom(techniqueName);
                if (instance != null) {
                    loaded.add(instance);
                    instance.setLevel(getLevel(techniqueName));
                }
            }
        }
        return loaded;
    }

    public void cast(double wx, double wy, Entity caster) {
        MagicSource cast = new MagicSource(wx, wy, caster, loadTechniques(), color);
        World.getRegion().addMagicSource(cast);
        EventDispatcher.invoke(new SpellCastEvent(this, cast));
    }

    public void addLevel(String technique) {
        setLevel(technique, getLevel(technique) + 1);
    }
    public void setLevel(String technique, int l) { levels.put(technique, l); }

    public void resetLevel(String technique) {
        levels.put(technique, 1);
    }

    public int getLevel(String technique) {
        if (levels.get(technique) == null) return 1;
        return levels.get(technique);
    }

    public void setColor(Color c) { this.color = c; }
    public void setIconIndex(int index) { iconIndex = index; }

    public int getIconIndex() {
        return iconIndex;
    }

    public Image getIcon() {
        return Assets.getImage("assets/gui/icons/spells/"+iconIndex+".png");
    }

    public Color getColor() {
        return color;
    }

    public boolean isEmpty() { return techniques.isEmpty(); }

    public ArrayList<String> getConflicts(String technique) {
        ArrayList<String> conflicts = new ArrayList<>();
        for (String t: techniques) {
            if (!t.equals(technique) && t.matches(Techniques.getConflictsWith(technique)))
                conflicts.add(t);
        }
        return conflicts;
    }

    public float getVolatility() {
        float conflicting = 0;
        for (String technique: techniques) conflicting += getConflicts(technique).isEmpty() ? 0 : 1;
        return conflicting / (float)techniques.size();
    }

    public int getCrystalCost() {
        int cost = 0;
        for (String technique: techniques) cost += Techniques.getCrystalCost(technique) * getLevel(technique);
        return cost;
    }

    public int getManaCost() {
        int cost = 0;
        for (String technique: techniques) cost += Techniques.getManaCost(technique) * getLevel(technique);
        return cost;
    }

    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        serialized.put("icon", iconIndex);
        for (String t: techniques) serialized.put(t, getLevel(t));
        return serialized;
    }

    public void deserialize(JSONObject json) {
        for (String technique: Techniques.getAll()) {
            if (json.get(technique) != null) {
                addTechnique(technique);
                setLevel(technique, (int)(long)json.get(technique));
            }
        }
        iconIndex = (int)(long)json.get("icon");
    }

}
