package world.entities.magic;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import world.World;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.Techniques;

import java.util.ArrayList;
import java.util.HashMap;

public class Spell {

    private String name;
    private ArrayList<String> techniques;
    private HashMap<String, Integer> levels;

    private Image icon;
    private boolean[][] pixels;
    private Color color;

    public Spell() {
        this.name = "Unnamed Spell";
        this.techniques = new ArrayList<>();
        this.levels = new HashMap<>();
        this.color = Color.white;
        this.pixels = new boolean[1][1];
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void addTechnique(String technique) { this.techniques.add(technique); }
    public void removeTechnique(String technique) { this.techniques.remove(technique); }
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
    }

    public void addLevel(String technique) {
        levels.put(technique, getLevel(technique) + 1);
    }

    public void resetLevel(String technique) {
        levels.put(technique, 1);
    }

    public int getLevel(String technique) {
        if (levels.get(technique) == null) return 1;
        return levels.get(technique);
    }

    public void setColor(Color c) { this.color = c; }
    public void setPixels(boolean[][] grid) { this.pixels = grid; }

    public Image getIcon() {
        if (icon != null) return icon;
        try {
            icon = new Image(pixels.length, pixels[0].length);
            Graphics b = icon.getGraphics();
            b.setColor(color);
            for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[0].length; j++) {
                    if (pixels[i][j]) b.fillRect(i, j, 1, 1);
                }
            }
            return icon;
        } catch (SlickException e) {
            e.printStackTrace();
        }
        return null;
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

}
