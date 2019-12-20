package world.particles;

import assets.Assets;
import assets.definitions.Definitions;
import assets.definitions.TileDefinition;
import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Chunk;
import world.World;

import java.util.ArrayList;

public class ParticleSource {

    public static final int UPDATES_PER_SECOND = 8;

    private double[] coordinates;
    private double direction, minRadius, maxRadius, fov, particleVelocity;
    private int ratePerSecond, particlesRemaining;
    private EmissionMode emissionMode;
    private Color[] colors;

    private long lastParticleSpawn;

    private ArrayList<Particle> particles;

    public ParticleSource() {
        this.lastParticleSpawn = System.currentTimeMillis();
        this.coordinates = new double[2];
        this.direction = 0;
        this.minRadius = 0;
        this.maxRadius = 0.25f;
        this.particlesRemaining = 200;
        this.ratePerSecond = 100;
        this.fov = 360;
        this.particleVelocity = 0.5f;
        this.emissionMode = EmissionMode.RADIATE;
        this.colors = new Color[]{Color.white, Color.gray, Color.lightGray};
        this.particles = new ArrayList<>();
    }

    public void update() {

        if (particlesRemaining <= 0) return;

        int amountToSpawn = (int)((System.currentTimeMillis() - lastParticleSpawn) / (1000 / ratePerSecond));

        for (int i = 0; i < amountToSpawn; i++) {
            lastParticleSpawn = System.currentTimeMillis();
            particlesRemaining--;
            double pdir = direction + MiscMath.random(-fov/2, fov/2);
            double[] p_off = MiscMath.getRotatedOffset(
                    0,
                    -(emissionMode == EmissionMode.RADIATE
                            ? minRadius + MiscMath.random(0, 0.2f)
                            : (emissionMode == EmissionMode.GRAVITATE
                                ? maxRadius
                                : MiscMath.random(minRadius, maxRadius))),
                    pdir);
            boolean fixed = Math.random() < 0.85;
            double[] p_pos = fixed ? coordinates : new double[]{coordinates[0], coordinates[1]};
            particles.add(new Particle(
                    emissionMode != EmissionMode.SCATTER ? particleVelocity : 0,
                            pdir + (emissionMode == EmissionMode.GRAVITATE ? 180 : 0),
                    (int)(1000 * ((maxRadius - minRadius) / particleVelocity)), p_pos, p_off, new Color(colors[(int)MiscMath.random(0, colors.length - 1)])));
        }
    }

    public void draw(float ox, float oy, float scale, Graphics g) {

        double mxosw = maxRadius * 2 * Chunk.TILE_SIZE * scale;
        double mnosw = minRadius * 2 * Chunk.TILE_SIZE * scale;
        double[] dir_offset = MiscMath.getRotatedOffset(0, -maxRadius * 2, direction);

        g.setColor(Color.red);
        g.fillOval((float)(ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - 2), (float)(oy + (coordinates[1] * Chunk.TILE_SIZE * scale) - 2), 4, 4);
        g.drawLine(
                (float)(ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - 2),
                (float)(oy + ((coordinates[1]) * Chunk.TILE_SIZE * scale) - 2),
                (float)(ox + ((coordinates[0] + dir_offset[0]) * Chunk.TILE_SIZE * scale) - 2),
                (float)(oy + ((coordinates[1] + dir_offset[1]) * Chunk.TILE_SIZE * scale) - 2));
        g.setColor(Color.blue);
        g.drawOval(
                (float)(ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - (mxosw/2)),
                (float)(oy + (coordinates[1] * Chunk.TILE_SIZE * scale) - (mxosw/2)),
                (float)mxosw,
                (float)mxosw);
        g.setColor(Color.cyan);
        g.drawOval(
                (float)(ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - (mnosw/2)),
                (float)(oy + (coordinates[1] * Chunk.TILE_SIZE * scale) - (mnosw/2)),
                (float)mnosw,
                (float)mnosw);

        for (int i = particles.size() - 1; i >= 0; i--) {

            Particle p = particles.get(i);
            if (p.isExpired()) { particles.remove(p); continue; }
            float alpha = 1 - (float)Math.abs(.5f - p.percentComplete() * 1.5f);
            double[] pcoords = p.getCoordinates();

            boolean behind_something = false;
            for (int t = 1; t < Assets.TILE_SPRITESHEET.getHeight() / 16; t++) {
                byte[] tile = World.getTile((int)pcoords[0], (int)pcoords[1] + t);
                TileDefinition td = Definitions.getTile(tile[1]);
                if (pcoords[1] > (pcoords[1] + t - td.getHeight())) { behind_something = true; break; }
            }
            if (behind_something) continue;

            p.getColor().a = alpha;
            g.setColor(p.getColor());
            g.fillRect(
                    (float)(ox + (pcoords[0] * Chunk.TILE_SIZE * scale) - (scale / 2)),
                    (float)(oy + (pcoords[1] * Chunk.TILE_SIZE * scale) - (scale / 2)),
                    scale,
                    scale
            );
        }

        g.setColor(Color.white);

    }

    public boolean isDepleted() { return particlesRemaining <= 0 && particles.size() == 0; }

    public double getDirection() { return direction; }

    public void addDirection(double angle) {
        setDirection(direction + angle);
    }

    public void setDirection(double angle) {
        this.direction = angle;
    }

    public double[] getCoordinates() { return coordinates; }

    public void setCoordinates(double x, double y) {
        this.coordinates[0] = x;
        this.coordinates[1] = y;
    }

    public void addCoordinates(double dx, double dy) {
        setCoordinates(coordinates[0] + dx, coordinates[1] + dy);
    }

    public void setArcLength(double degrees) { this.fov = degrees; }

    public double getArcLength() { return fov; }

    public void setEmissionMode(EmissionMode emissionMode) { this.emissionMode = emissionMode; }

    public String debug() {
        double[] pos = MiscMath.getRotatedOffset(
                0,
                emissionMode == EmissionMode.RADIATE
                        ? minRadius
                        : (emissionMode == EmissionMode.GRAVITATE
                            ? maxRadius
                            : MiscMath.random(minRadius, maxRadius)),
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
        this.emissionTime = System.currentTimeMillis();
        this.startPosition = startPosition;
        this.startOffset = startOffset;
        this.velocity = velocity;
        this.direction = direction;
        this.lifetime = lifetime;
        this.color = color;
    }

    public Color getColor() { return color; }
    public double percentComplete() { return (System.currentTimeMillis() - emissionTime) / (double)lifetime; }
    public boolean isExpired() { return System.currentTimeMillis() - emissionTime > lifetime; }
    public double getElapsedSeconds() { return (System.currentTimeMillis() - emissionTime) / 1000f; }
    public double[] getCoordinates() {
        return getRelativeCoordinates(startPosition);
    }
    private double[] getRelativeCoordinates(double[] origin) {
        double[] offset = MiscMath.getRotatedOffset(0, -(velocity * getElapsedSeconds()), direction);
        return new double[]{ origin[0] + startOffset[0] + offset[0], origin[1] + startOffset[1] + offset[1] };
    }

}
