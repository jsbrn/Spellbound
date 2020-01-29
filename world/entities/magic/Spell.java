package world.entities.magic;

import world.Region;
import world.World;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.TechniqueName;

import java.util.ArrayList;

public class Spell {

    private String name;
    private ArrayList<TechniqueName> techniques;

    public Spell() {
        this.name = "Unnamed Spell";
        this.techniques = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTechnique(TechniqueName technique) { this.techniques.add(technique); }
    public void removeTechnique(TechniqueName technique) { this.techniques.remove(technique); }
    public boolean hasTechnique(TechniqueName techniqueName) { return techniques.contains(techniqueName); }

    private ArrayList<Technique> loadTechniques() {
        ArrayList<Technique> loaded = new ArrayList<>();
        for (TechniqueName techniqueName: techniques) loaded.add(Technique.create(techniqueName));
        return loaded;
    }

    public void cast(double wx, double wy, Entity caster) {
        //maybe add something to catch the spell-level techniques (like spawn patterns and chaining)
        MagicSource cast = new MagicSource(wx, wy, caster, loadTechniques());
        World.getRegion().addMagicSource(cast);
    }

}
