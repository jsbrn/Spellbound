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
    private int ratePerFrame, maxParticleCount;
    private EmissionMode emissionMode;
    private Color[] colors;

    private ArrayList<Particle> particles;

    public ParticleSource() {
        this.coordinates = new float[2];
        this.lifespan = 10;
        this.direction = 0;
        this.minRadius = 0f;
        this.maxRadius = 0.5f;
        this.maxParticleCount = 5000;
        this.ratePerFrame = 20;
        this.fov = 10;
        this.particleVelocity = 1f;
        this.emissionMode = EmissionMode.RADIATE;
        this.colors = new Color[]{Color.orange, Color.red, Color.yellow};
        this.particles = new ArrayList<>();
    }

    public void update() {
        int amountToSpawn = ratePerFrame;
        for (int i = 0; i < amountToSpawn; i++) {
            float pdir = direction + (float)MiscMath.random(-fov/2, fov/2);
            float[] pos = MiscMath.getRotatedOffset(
                    0,
                    -(emissionMode == EmissionMode.RADIATE
                            ? minRadius + MiscMath.random(0, 0.2f)
                            : (emissionMode == EmissionMode.GRAVITATE
                                ? maxRadius
                                : MiscMath.random(minRadius, maxRadius))),
                    pdir);
            pos[0] += coordinates[0];
            pos[1] += coordinates[1];
            System.out.println(pos[0] + ", " + pos[1]);
            particles.add(new Particle(
                    emissionMode != EmissionMode.SCATTER ? particleVelocity : 0,
                            pdir + (emissionMode == EmissionMode.GRAVITATE ? 180 : 0),
                    (int)(1000 * ((maxRadius - minRadius) / particleVelocity)), pos, new Color(colors[(int)MiscMath.random(0, colors.length - 1)])));
        }
    }

    public void draw(float ox, float oy, float scale, Graphics g) {

        float mxosw = maxRadius * 2 * Chunk.TILE_SIZE * scale;
        float mnosw = minRadius * 2 * Chunk.TILE_SIZE * scale;
        float[] dir_offset = MiscMath.getRotatedOffset(0, -maxRadius * 2, direction);

        g.setColor(Color.red);
        g.fillOval(ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - 2, oy + (coordinates[1] * Chunk.TILE_SIZE * scale) - 2, 4, 4);
        g.drawLine(
                ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - 2,
                oy + ((coordinates[1]) * Chunk.TILE_SIZE * scale) - 2,
                ox + ((coordinates[0] + dir_offset[0]) * Chunk.TILE_SIZE * scale) - 2,
                oy + ((coordinates[1] + dir_offset[1]) * Chunk.TILE_SIZE * scale) - 2);
        g.setColor(Color.blue);
        g.drawOval(ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - (mxosw/2), oy + (coordinates[1] * Chunk.TILE_SIZE * scale) - (mxosw/2), mxosw, mxosw);
        g.setColor(Color.cyan);
        g.drawOval(ox + (coordinates[0] * Chunk.TILE_SIZE * scale) - (mnosw/2), oy + (coordinates[1] * Chunk.TILE_SIZE * scale) - (mnosw/2), mnosw, mnosw);

        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            if (p.isExpired()) { particles.remove(p); continue; }
            float alpha = 1 - Math.abs(.5f - p.percentComplete() * 1.5f);
            p.getColor().a = alpha;
            g.setColor(p.getColor());
            float[] pcoords = p.getCurrentCoordinates();
            g.fillRect(
                    ox + (pcoords[0] * Chunk.TILE_SIZE * scale) - (scale / 2),
                    oy + (pcoords[1] * Chunk.TILE_SIZE * scale) - (scale / 2),
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
    private Color color;

    public Particle(float velocity, float direction, int lifetime, float[] startPosition, Color color) {
        this.emissionTime = System.currentTimeMillis();
        this.startPosition = startPosition;
        this.velocity = velocity;
        this.direction = direction;
        this.lifetime = lifetime;
        this.color = color;
    }

    public Color getColor() { return color; }
    public float percentComplete() { return (float)(System.currentTimeMillis() - emissionTime) / (float)lifetime; }
    public boolean isExpired() { return System.currentTimeMillis() - emissionTime > lifetime; }
    public float getElapsedSeconds() { return (float)(System.currentTimeMillis() - emissionTime) / 1000f; }
    public float[] getCurrentCoordinates() {
        float[] offset = MiscMath.getRotatedOffset(0, -(velocity * getElapsedSeconds()), direction);
        return new float[]{ startPosition[0] + offset[0], startPosition[1] + offset[1] };
    }

}

enum EmissionMode {
    RADIATE, GRAVITATE, SCATTER
}
