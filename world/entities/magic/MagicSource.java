package world.entities.magic;

import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.particles.ParticleSource;

import java.util.ArrayList;

public class MagicSource {

    private Entity caster;
    private ArrayList<Technique> techniques;
    private ArrayList<Entity> targets;
    private ParticleSource body;
    private double[] castCoordinates, moveTarget;
    private double moveSpeed, rotateSpeed, targetDirection;
    private MagicSource next;

    public MagicSource(double x, double y, Entity caster, ArrayList<Technique> techniques, Color color) {
        this.castCoordinates = new double[]{x, y};
        this.moveTarget = new double[]{caster.getLocation().getCoordinates()[0], caster.getLocation().getCoordinates()[1]};
        this.targetDirection = 0;
        this.moveSpeed = 5;
        this.rotateSpeed = 45;
        this.caster = caster;
        this.techniques = techniques;
        this.body = new ParticleSource();
        this.body.setColor(color);
        this.body.setCoordinates(caster.getLocation().getCoordinates()[0], caster.getLocation().getCoordinates()[1] - 0.5f);
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

        double rdx = rotateSpeed;
        body.addDirection(MiscMath.getConstant(targetDirection > body.getDirection() ? rdx : -rdx, 1));
        body.update();
    }

    public void draw(float osx, float osy, float scale) {
        body.draw(osx, osy, scale);
    }

    public Entity getCaster() { return caster; }

    public double[] getCastCoordinates() { return castCoordinates; }

    public void setMoveTarget(double x, double y) {
        moveTarget[0] = x;
        moveTarget[1] = y;
    }

    public void setEntityTargets(ArrayList<Entity> entities) {
        targets = entities;
    }

    public double getRotationSpeed() { return rotateSpeed; }
    public double getTargetDirection() { return targetDirection; }

    public void setTargetDirection(double angle) {
        this.targetDirection = angle;
    }
    public void setDirection(double angle) { this.body.setDirection(angle); }

    public ParticleSource getBody() { return body; }

}
