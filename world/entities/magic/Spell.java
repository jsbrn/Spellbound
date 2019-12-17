package world.entities.magic;

import world.World;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;

import java.util.ArrayList;

public class Spell {

    private ArrayList<Technique> techniques;

    public Spell() {
        this.techniques = new ArrayList<>();
    }

    public void addTechnique(Technique technique) { this.techniques.add(technique); }

    public void cast(double x, double y, Entity caster) {
        //maybe add something to catch the spell-level techniques (like spawn patterns and chaining)
        MagicSource cast = new MagicSource(x, y, caster, techniques);
        World.addMagicSource(cast);
    }

}
