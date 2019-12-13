package world.particles;

import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Chunk;

import java.util.ArrayList;

public class ParticleSource {

    public static final int UPDATES_PER_SECOND = 8;

    private float[] coordinates;
    private float direction, minRadius, maxRadius, fov, lifespan, particleVelocity;
    private int ratePerSecond, maxParticleCount;
    private EmissionMode emissionMode;

    private ArrayList<Particle> particles;

    public ParticleSource() {
        this.coordinates = new float[2];
        this.lifespan = 10;
        this.direction = 0;
        this.minRadius = 0;
        this.maxRadius = 0.25f;
        this.maxParticleCount = 50;
        this.ratePerSecond = 1;
        this.fov = 360;
        this.particleVelocity = 0.1f;
        this.emissionMode = EmissionMode.RADIATE;
        this.particles = new ArrayList<>();
    }

    public void update() {
        int amountToSpawn = ratePerSecond;
        for (int i = 0; i < amountToSpawn; i++) {
            float[] pos = MiscMath.getRotatedOffset(
                    0,
                    emissionMode == EmissionMode.RADIATE
                            ? minRadius
                            : (emissionMode == EmissionMode.GRAVITATE
                                ? maxRadius
                                : MiscMath.random(minRadius, maxRadius)),
                    direction + MiscMath.random(-fov/2, fov/2));
            particles.add(new Particle(particleVelocity, direction, 1000, pos));
        }
    }

    public void draw(float ox, float oy, float scale, Graphics g) {

        float mxosw = maxRadius * 2 * Chunk.TILE_SIZE * scale;
        float mnosw = minRadius * 2 * Chunk.TILE_SIZE * scale;

        g.setColor(Color.red);
        g.fillOval(ox + (coordinates[0] * scale) - 2, oy + (coordinates[1] * scale) - 2, 4, 4);
        g.setColor(Color.blue);
        g.drawOval(ox + (coordinates[0] * scale) - (mxosw/2), oy + (coordinates[1] * scale) - (mxosw/2), mxosw, mxosw);
        g.setColor(Color.cyan);
        g.drawOval(ox + (coordinates[0] * scale) - (mnosw/2), oy + (coordinates[1] * scale) - (mnosw/2), mnosw, mnosw);

        g.setColor(Color.white);
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            if (p.isExpired()) { particles.remove(p); continue; }
            float[] pcoords = p.getCurrentCoordinates();
            g.fillRect(
                    ox + ((coordinates[0] + pcoords[0]) * scale),
                    oy + ((coordinates[1] + pcoords[1]) * scale),
                    scale,
                    scale
            );
        }

    }

    public void setCoordinates(float x, float y) {
        this.coordinates[0] = x;
        this.coordinates[1] = y;
    }

    public String debug() {
        float[] pos = MiscMath.getRotatedOffset(
                0,
                emissionMode == EmissionMode.RADIATE
                        ? minRadius
                        : (emissionMode == EmissionMode.GRAVITATE
                            ? maxRadius
                            : MiscMath.random(minRadius, maxRadius)),
                direction);
        if (particles.isEmpty()) return "";
        return particles.size() + ", spawn = " + pos[0] +", "+ pos[1] + ", p(0) = " + particles.get(0).getCurrentCoordinates()[0] + ", " + particles.get(0).getCurrentCoordinates()[1];
    }

}

class Particle {

    private long emissionTime, lifetime;
    private float velocity, direction;
    private float[] startPosition;

    public Particle(float velocity, float direction, int lifetime, float[] startPosition) {
        this.emissionTime = System.currentTimeMillis();
        this.startPosition = startPosition;
        this.velocity = velocity;
        this.direction = direction;
        this.lifetime = lifetime;
    }

    public boolean isExpired() { return System.currentTimeMillis() - emissionTime > lifetime; }
    public float getElapsedSeconds() { return (float)(System.currentTimeMillis() - emissionTime) / 1000f; }
    public float[] getCurrentCoordinates() {
        float[] offset = MiscMath.getRotatedOffset(0, (velocity * getElapsedSeconds()), direction);
        return new float[]{ startPosition[0] + offset[0], startPosition[1] + offset[1] };
    }

}

enum EmissionMode {
    RADIATE, GRAVITATE, SCATTER
}
