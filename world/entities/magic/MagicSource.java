package world.entities.magic;

import misc.MiscMath;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.particles.ParticleSource;

import java.util.ArrayList;

public class MagicSource {

    private Entity caster;
    private ArrayList<Technique> techniques;
    private ParticleSource body;
    private double[] castCoordinates, moveTarget;
    private double moveSpeed;
    private MagicSource next;

    public MagicSource(double x, double y, Entity caster, ArrayList<Technique> techniques) {
        this.castCoordinates = new double[]{x, y};
        this.moveTarget = new double[]{x, y};
        this.moveSpeed = 5;
        this.caster = caster;
        this.techniques = techniques;
        this.body = new ParticleSource();
        this.body.setCoordinates(caster.getCoordinates()[0] + 0.5f, caster.getCoordinates()[1]);
        for (Technique t: techniques) t.applyTo(this);
    }

    public void update() {
        for (Technique t: techniques) t.update(this);
        double[] unitVector = MiscMath.getUnitVector(
                (float)(moveTarget[0] - body.getCoordinates()[0]),
                (float)(moveTarget[1] - body.getCoordinates()[1]));
        body.addCoordinates(
            unitVector[0] * MiscMath.getConstant(moveSpeed, 1),
            unitVector[1] * MiscMath.getConstant(moveSpeed, 1)
        );
        body.update();
    }

    public void draw(float ox, float oy, float scale, Graphics g) {
        body.draw(ox, oy, scale, g);
    }

    public Entity getCaster() { return caster; }

    public double[] getCastCoordinates() { return castCoordinates; }

    public void setMoveTarget(double x, double y) {
        moveTarget[0] = x;
        moveTarget[1] = y;
    }

    public ParticleSource getBody() { return body; }

}
