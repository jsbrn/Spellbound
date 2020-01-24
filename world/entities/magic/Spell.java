package world.entities.magic;

import world.Region;
import world.World;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;

import java.util.ArrayList;

public class Spell {

    private String name;
    private ArrayList<Technique> techniques;

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

    public void addTechnique(Technique technique) { this.techniques.add(technique); }

    public void cast(double wx, double wy, Entity caster) {
        //maybe add something to catch the spell-level techniques (like spawn patterns and chaining)
        MagicSource cast = new MagicSource(wx, wy, caster, techniques);
        World.getRegion().addMagicSource(cast);
    }

}
