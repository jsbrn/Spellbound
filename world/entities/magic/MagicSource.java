package world.entities.magic;

import assets.definitions.Definitions;
import misc.Location;
import misc.MiscMath;
import org.newdawn.slick.Color;
import world.Tiles;
import world.entities.Entity;
import world.entities.magic.techniques.Technique;
import world.entities.magic.techniques.Techniques;
import world.entities.magic.techniques.effects.EffectTechnique;
import world.entities.types.humanoids.HumanoidEntity;
import world.events.EventDispatcher;
import world.events.event.MagicDepletedEvent;
import world.events.event.MagicImpactEvent;
import world.particles.ParticleSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MagicSource {

    private Entity caster, target;
    private ArrayList<Technique> techniques;
    private ParticleSource body;
    private double[] castCoordinates, moveTarget;
    private double moveSpeed, torque, energy;
    private MagicSource next;

    private List<Entity> lastColliding;
    private boolean lastLocationSolid;

    public MagicSource(double x, double y, Entity caster, ArrayList<Technique> techniques, Color color) {
        this.lastColliding = new ArrayList<Entity>();
        this.castCoordinates = new double[]{x, y};
        this.energy = 0.5;
        this.moveSpeed = 3;
        this.torque = 1;
        this.caster = caster;
        this.techniques = techniques;
        this.body = new ParticleSource();
        this.body.setColor(color);
        this.body.setLocation(new Location(caster.getLocation()));
        double[] offset = MiscMath.getRotatedOffset(0, -0.6, MiscMath.angleBetween(caster.getLocation().getCoordinates()[0], caster.getLocation().getCoordinates()[1], x, y));
        this.moveTarget = new double[]{body.getLocation().getCoordinates()[0] + offset[0], body.getLocation().getCoordinates()[1] + offset[1]};
        List<Entity> found = caster.getLocation().getRegion().getEntities((int)x-1, (int)y-1, 3, 3).stream().filter(e -> !e.equals(caster)).collect(Collectors.toList());
        this.target = found.isEmpty() ? null : found.get(0);
        for (Technique t: techniques) t.applyTo(this);
    }

    public void update() {

        //invoke collision events
        List<Entity> colliding = getCollidingEntities();
        colliding.stream()
                .filter(e -> !e.equals(caster) && !lastColliding.contains(e))
                .forEach(e -> {
                    if (e.isTile()) return;
                    if ((e instanceof HumanoidEntity && !((HumanoidEntity) e).isAlliedTo((HumanoidEntity)caster)))
                        EventDispatcher.invoke(new MagicImpactEvent(this, e));
                });
        lastColliding = colliding;
        byte[] currentTile = getBody().getLocation().getRegion().getTile(
                (int)getBody().getLocation().getCoordinates()[0],
                (int)getBody().getLocation().getCoordinates()[1]
        );
        boolean currentSolid = Tiles.collides(currentTile[1]);
        if (!lastLocationSolid && currentSolid) EventDispatcher.invoke(new MagicImpactEvent(this, null));
        lastLocationSolid = currentSolid;

        //move and update particle body
        double[] unitVector = MiscMath.getUnitVector(
                (float)(moveTarget[0] - body.getLocation().getCoordinates()[0]),
                (float)(moveTarget[1] - body.getLocation().getCoordinates()[1]));
        body.getLocation().addCoordinates(
            unitVector[0] * MiscMath.getConstant(moveSpeed, 1),
            unitVector[1] * MiscMath.getConstant(moveSpeed, 1)
        );
        if (body.getLocation().distanceTo(moveTarget[0], moveTarget[1]) < 0.1)
            body.getLocation().setCoordinates(moveTarget[0], moveTarget[1]);
        body.update();

        //update techniques
        for (Technique t: techniques) if (energy > 0 || !Techniques.getRequiresEnergy(t.getID())) t.update(this);

        //handle energy depletion and events
        double energyDelta = MiscMath.getConstant(-1, 1);
        if (energy > 0 && energy + energyDelta <= 0) {
            EventDispatcher.invoke(new MagicDepletedEvent(this));
        }
        energy = MiscMath.clamp(energy + energyDelta, 0, Integer.MAX_VALUE);
        if (energy <= 0) {
            body.stop();
        }

    }

    public boolean hasTechnique(String technique) {
        return !techniques.stream().filter(t -> t.getID().equals(technique)).collect(Collectors.toList()).isEmpty();
    }

    public int getLevel(String technique) {
        List<Technique> matches = techniques.stream().filter(t -> t.getID().equals(technique)).collect(Collectors.toList());
        return matches.isEmpty() ? 0 : matches.get(0).getLevel();
    }

    public void draw(float osx, float osy, float scale) {
        body.draw(osx, osy, scale);
    }

    public void setMoveTarget(double x, double y) {
        moveTarget[0] = x;
        moveTarget[1] = y;
    }

    public void affectOnce() {
        for (Technique t: techniques)
            if (t instanceof EffectTechnique)
                ((EffectTechnique) t).affectOnce(this);
    }

    public void affectContinuous() {
        for (Technique t: techniques)
            if (t instanceof EffectTechnique)
                ((EffectTechnique) t).affectContinuous(this);
    }

    public List<Entity> getCollidingEntities() {
        List<Entity> inner = body.getLocation().getRegion().getEntities(
                body.getLocation().getCoordinates()[0],
                body.getLocation().getCoordinates()[1],
                body.getReachRadius()
        ), outer = body.getLocation().getRegion().getEntities(
                body.getLocation().getCoordinates()[0],
                body.getLocation().getCoordinates()[1],
                body.getDepthRadius()
        );
        return outer.stream().filter(e -> !(inner.contains(e) && !outer.contains(e))).collect(Collectors.toList());
    }

    public List<MagicSource> getCollidingMagic() {
        List<MagicSource> inner = body.getLocation().getRegion().getMagicSources(
                body.getLocation().getCoordinates()[0],
                body.getLocation().getCoordinates()[1],
                body.getReachRadius()
        ), outer = body.getLocation().getRegion().getMagicSources(
                body.getLocation().getCoordinates()[0],
                body.getLocation().getCoordinates()[1],
                body.getDepthRadius()
        );
        return outer.stream().filter(e -> !(inner.contains(e) && !outer.contains(e))).collect(Collectors.toList());
    }

    public void setEnergy(double e) { energy = e; }
    public double getEnergy() { return energy; }
    public void addEnergy(double amount) { setEnergy(energy + amount);}
    public double getTorque() { return torque; }
    public void setTorque(double t) { torque = t; }
    public void setMoveSpeed(double t) { moveSpeed = MiscMath.clamp(t, 0, Integer.MAX_VALUE); }
    public void addMoveSpeed(double t) { setMoveSpeed(moveSpeed + t); }

    public Entity getCaster() { return caster; }
    public double[] getCastCoordinates() { return castCoordinates; }
    public void setTarget(Entity target) { this.target = target; }
    public Entity getTarget() { return target; }

    public void addDirection(double angle) { body.addDirection(angle);}
    public void setDirection(double angle) { body.setDirection(angle); }
    public ParticleSource getBody() { return body; }

}
