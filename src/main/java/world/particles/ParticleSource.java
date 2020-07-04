package world.particles;

import assets.Assets;
import com.github.mathiewz.slick.Color;
import com.github.mathiewz.slick.Graphics;
import misc.Location;
import misc.MiscMath;
import misc.Window;
import network.MPClient;
import world.Camera;
import world.Chunk;
import world.Tiles;
import world.World;

import java.util.ArrayList;

public class ParticleSource {

    private Location location;
    private double direction, reachRadius, depthRadius, fov, particleVelocity;
    private int density;
    private EmissionMode emissionMode;
    private Color[] colors;
    private boolean allowParticleSpawning;

    private double nextParticleReady;

    private ArrayList<Particle> particles;

    public ParticleSource() {
        this.reachRadius = 0;
        this.depthRadius = 0.25f;
        this.density = 1;
        this.allowParticleSpawning = true;
        this.fov = 360;
        this.particleVelocity = 1;
        this.emissionMode = EmissionMode.SCATTER;
        this.colors = new Color[]{Color.white, Color.gray, Color.lightGray};
        this.particles = new ArrayList<>();
    }

    public void interpolate() {

        if (!isOnScreen()) return;

        if (!allowParticleSpawning) return;
        double area = 3.14 * (Math.pow(depthRadius + reachRadius, 2) - Math.pow(reachRadius, 2));
        double maxAtOnce = (area * Chunk.TILE_SIZE) * density * 15;
        double amountToSpawn = MiscMath.getConstant(maxAtOnce, 1); //one "pixel"
        nextParticleReady += amountToSpawn;
        if (nextParticleReady < 1 || particles.size() > maxAtOnce) return;
        for (int i = 0; i < nextParticleReady; i++) {
            double pdir = direction + MiscMath.random(-fov/2, fov/2);
            double[] p_off = MiscMath.getRotatedOffset(
                    0,
                    -(emissionMode == EmissionMode.RADIATE
                            ? MiscMath.random(reachRadius, reachRadius + depthRadius)
                            : (emissionMode == EmissionMode.GRAVITATE
                                ? MiscMath.random(reachRadius, reachRadius + depthRadius)
                                : MiscMath.random(reachRadius, reachRadius + depthRadius))),
                    pdir);
            boolean fixed = Math.random() < 0.85;
            double[] p_pos = fixed ? location.getCoordinates() : new double[]{location.getCoordinates()[0], location.getCoordinates()[1]};
            particles.add(new Particle(
                    emissionMode != EmissionMode.SCATTER ? particleVelocity : 0,
                    pdir + (emissionMode == EmissionMode.GRAVITATE ? 180 : 0),
                    !fixed || emissionMode == EmissionMode.SCATTER ? 500 : (int)(1000 * ((depthRadius - reachRadius) / particleVelocity)),
                    p_pos,
                    p_off,
                    new Color(colors[(int)MiscMath.random(0, colors.length - 1)])));
        }
        nextParticleReady = 0;
    }

    public void draw(float ox, float oy, float scale) {

        if (!isOnScreen()) return;

        for (int i = particles.size() - 1; i >= 0; i--) {

            Particle p = particles.get(i);
            if (p.isExpired()) { particles.remove(p); continue; }
            float alpha = 1 - (float)Math.abs(.5f - p.percentComplete() * 1.5f);
            double[] pcoords = p.getCoordinates();

            boolean behind_something = false;
            for (int t = 1; t < Assets.TILE_SPRITESHEET.getHeight() / Chunk.TILE_SIZE; t++) {
                byte[] tile = Camera.getLocation().getRegion().getTile((int)pcoords[0], (int)pcoords[1] + t);
                if (pcoords[1] > (pcoords[1] + t - Tiles.getHeight(tile[1]))) {
                    if (Tiles.getTransparency(tile[1]) == 0) behind_something = true; else alpha = Tiles.getTransparency(tile[1]);
                    break;
                }
            }
            if (behind_something) continue;

            p.getColor().a = alpha;
            float osx = (float)(ox + (pcoords[0] * Chunk.TILE_SIZE * scale) - (scale / 2));
            float osy = (float)(oy + (pcoords[1] * Chunk.TILE_SIZE * scale) - (scale / 2));

            Assets.PARTICLE.drawEmbedded(osx, osy, osx + scale, osy + scale, 0, 0, 1, 1, p.getColor());

        }
    }

    public void drawDebug(double ox, double oy, float scale, Graphics g) {

        float[] osc = Camera.getOnscreenCoordinates(ox + location.getCoordinates()[0], oy + location.getCoordinates()[1], scale);

        double mxosw = (reachRadius + depthRadius) * 2 * Chunk.TILE_SIZE * scale;
        double mnosw = reachRadius * 2 * Chunk.TILE_SIZE * scale;
        double[] dir_offset = MiscMath.getRotatedOffset(0, -depthRadius * 2, direction);

        g.setColor(Color.red);
        g.fillOval(osc[0] - 2, osc[1] - 2, 4, 4);
        g.drawLine(
                osc[0] - 2,
                (float) (osc[1] - 2),
                (float) (osc[0] + (dir_offset[0] * Chunk.TILE_SIZE * scale) - 2),
                (float) (osc[1] + (dir_offset[1] * Chunk.TILE_SIZE * scale) - 2));
        g.setColor(Color.blue);
        g.drawOval(
                osc[0] - (float)(mxosw / 2),
                (float) (osc[1] - (mxosw / 2)),
                (float) mxosw,
                (float) mxosw);
        g.setColor(Color.cyan);
        g.drawOval(
                osc[0] - (float)(mnosw / 2),
                (float) (osc[1] - (mnosw / 2)),
                (float) mnosw,
                (float) mnosw);

    }

    private boolean isOnScreen() {
        float[] osc = Camera.getOnscreenCoordinates(location.getCoordinates()[0], location.getCoordinates()[1], Window.getScale());
        return MiscMath.circlesIntersect(
                osc[0], osc[1],
                (getDepthRadius() + getReachRadius()) * Window.getScale(), Window.getWidth() / 2, Window.getHeight() / 2, Window.getWidth());

    }

    public double getDirection() { return direction; }

    public void addDirection(double angle) {
        setDirection(direction + angle);
    }

    public void setDirection(double angle) {
        this.direction = angle;
    }

    public void setLocation(Location l) { this.location = l; }

    public Location getLocation() { return location; }

    public void setColor(Color base) {
        this.colors = new Color[]{base, base.darker(), base.brighter()};
    }

    public void setArcLength(double degrees) { this.fov = degrees; }

    public double getArcLength() { return fov; }

    public void setEmissionMode(EmissionMode emissionMode) { this.emissionMode = emissionMode; }

    public void setReachRadius(double reachRadius) {
        this.reachRadius = reachRadius;
    }

    public void setDepthRadius(double depthRadius) {
        this.depthRadius = depthRadius;
    }

    public double getReachRadius() {
        return reachRadius;
    }

    public double getDepthRadius() {
        return depthRadius;
    }

    public void setDensity(int d) { density = d; }

    public void addMinRadius(double amount) {
        this.reachRadius = MiscMath.clamp(reachRadius + amount, 0, Integer.MAX_VALUE);
    }

    public void addDepthRadius(double amount) {
        this.depthRadius = MiscMath.clamp(depthRadius + amount, 0, Integer.MAX_VALUE);
    }

    public void stop() {
        allowParticleSpawning = false;
    }

    public boolean isSpawning() {
        return allowParticleSpawning;
    }

    public boolean isEmpty() { return particles.isEmpty(); }

    public String debug() {
        double[] pos = MiscMath.getRotatedOffset(
                0,
                emissionMode == EmissionMode.RADIATE
                        ? reachRadius
                        : (emissionMode == EmissionMode.GRAVITATE
                            ? depthRadius
                            : MiscMath.random(reachRadius, depthRadius)),
                direction);
        if (particles.isEmpty()) return "";
        return particles.size() + ", spawn = " + pos[0] +", "+ pos[1] + ", p(0) = " + particles.get(0).getCoordinates()[0] + ", " + particles.get(0).getCoordinates()[1];
    }

}

class Particle {

    private long emissionTime, lifetime;
    private double velocity, direction;
    private double[] startPosition, startOffset;
    private Color color;

    public Particle(double velocity, double direction, int lifetime, double[] startPosition, double[] startOffset, Color color) {
        this.emissionTime = MPClient.getWorld().getCurrentTime();
        this.startPosition = startPosition;
        this.startOffset = startOffset;
        this.velocity = velocity;
        this.direction = direction;
        this.lifetime = lifetime;
        this.color = color;
    }

    public Color getColor() { return color; }
    public double percentComplete() { return (MPClient.getWorld().getCurrentTime() - emissionTime) / (double)lifetime; }
    public boolean isExpired() { return MPClient.getWorld().getCurrentTime() - emissionTime > lifetime; }
    public double getElapsedSeconds() { return (MPClient.getWorld().getCurrentTime() - emissionTime) / 1000f; }
    public double[] getCoordinates() {
        return getRelativeCoordinates(startPosition);
    }
    private double[] getRelativeCoordinates(double[] origin) {
        double[] offset = MiscMath.getRotatedOffset(0, -(velocity * getElapsedSeconds()), direction);
        return new double[]{ origin[0] + startOffset[0] + offset[0], origin[1] + startOffset[1] + offset[1] };
    }

}
