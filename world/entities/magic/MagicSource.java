package world.entities.magic;

import misc.Location;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.particles.ParticleSource;

import java.util.ArrayList;

public class MagicSource {

    private Entity caster, target;
    private ArrayList<Technique> techniques;
    private ParticleSource body;
    private double[] castCoordinates, moveTarget;
    private double moveSpeed, rotateSpeed, targetDirection, energy;
    private MagicSource next;

    public MagicSource(double x, double y, Entity caster, ArrayList<Technique> techniques, Color color) {
        this.castCoordinates = new double[]{x, y};
        this.energy = 1;
        this.moveTarget = new double[]{caster.getLocation().getCoordinates()[0], caster.getLocation().getCoordinates()[1] - 0.5f};
        this.targetDirection = 0;
        this.moveSpeed = 5;
        this.rotateSpeed = 45;
        this.caster = caster;
        this.techniques = techniques;
        this.body = new ParticleSource();
        this.body.setColor(color);
        this.body.setLocation(new Location(caster.getLocation()));
        this.body.getLocation().addCoordinates(0, -0.5);
        for (Technique t: techniques) t.applyTo(this);
    }

    public void update() {
        if (energy > 0) for (Technique t: techniques) t.update(this);
        double[] unitVector = MiscMath.getUnitVector(
                (float)(moveTarget[0] - body.getLocation().getCoordinates()[0]),
                (float)(moveTarget[1] - body.getLocation().getCoordinates()[1]));
        body.getLocation().addCoordinates(
            unitVector[0] * MiscMath.getConstant(moveSpeed, 1),
            unitVector[1] * MiscMath.getConstant(moveSpeed, 1)
        );

        double rdx = rotateSpeed;
        body.addDirection(MiscMath.getConstant(targetDirection > body.getDirection() ? rdx : -rdx, 1));
        body.update();

        energy = MiscMath.clamp(energy + MiscMath.getConstant(-1, 1), 0, Integer.MAX_VALUE);
        if (energy <= 0) body.stop();

    }

    public void draw(float osx, float osy, float scale) {
        body.draw(osx, osy, scale);
    }

    public void setMoveTarget(double x, double y) {
        moveTarget[0] = x;
        moveTarget[1] = y;
    }

    public double getEnergy() { return energy; }

    public Entity getCaster() { return caster; }
    public double[] getCastCoordinates() { return castCoordinates; }
    public void setTarget(Entity target) { this.target = target; }
    public Entity getTarget() { return target; }

    public double getRotationSpeed() { return rotateSpeed; }
    public double getTargetDirection() { return targetDirection; }
    public void setTargetDirection(double angle) {
        this.targetDirection = angle;
    }
    public void setDirection(double angle) { this.body.setDirection(angle); }
    public ParticleSource getBody() { return body; }

}
