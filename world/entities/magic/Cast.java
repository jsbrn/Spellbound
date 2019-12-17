package world.entities.magic;

import world.particles.ParticleSource;

import java.util.ArrayList;

public class Cast {

    private ArrayList<Technique> techniques;
    private ParticleSource body;

    public Cast() {
        this.techniques = new ArrayList<>();
        this.body = new ParticleSource();
    }

    public void applyTechnique(Technique t) {
        this.techniques.add(t);
        t.apply(this);
    }

    public void update() {
        for (Technique t: techniques) t.update(this);
    }

    public ParticleSource getBody() {
        return body;
    }

}
