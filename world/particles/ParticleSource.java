package world.particles;

import misc.MiscMath;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import world.Chunk;

import java.util.ArrayList;

public class ParticleSource {

    public static final int UPDATES_PER_SECOND = 8;

    private float[] coordinates;
    private float direction, minRadius, maxRadius, fov, particleVelocity;
    private int ratePerFrame, particlesRemaining;
    private EmissionMode emissionMode;
    private Color[] colors;

    private ArrayList<Particle> particles;

    public ParticleSource() {
        this.coordinates = new float[2];
        this.direction = 0;
        this.minRadius = 0;
        this.maxRadius = 0.25f;
        this.particlesRemaining = 100;
        this.ratePerFrame = 5;
        this.fov = 360;
        this.particleVelocity = 0.25f;
        this.emissionMode = EmissionMode.RADIATE;
        this.colors = new Color[]{Color.white, Color.gray, Color.lightGray};
        this.particles = new ArrayList<>();
    }

    public void update() {
        if (particles.size() > particlesRemaining) return;
        for (int i = 0; i < ratePerFrame; i++) {
            particlesRemaining--;
            float pdir = direction + (float)MiscMath.random(-fov/2, fov/2);
            float[] p_off = MiscMath.getRotatedOffset(
                    0,
                    -(emissionMode == EmissionMode.RADIATE
                            ? minRadius + MiscMath.random(0, 0.2f)
                            : (emissionMode == EmissionMode.GRAVITATE
                                ? maxRadius
                                : MiscMath.random(minRadius, maxRadius))),
                    pdir);
            boolean fixed = Math.random() < 0.85;
            float[] p_pos = fixed ? coordinates : new float[]{coordinates[0], coordinates[1]};
            particles.add(new Particle(
                    emissionMode != EmissionMode.SCATTER ? particleVelocity : 0,
                            pdir + (emissionMode == EmissionMode.GRAVITATE ? 180 : 0),
                    (int)(1000 * ((maxRadius - minRadius) / particleVelocity)), p_pos, p_off, new Color(colors[(int)MiscMath.random(0, colors.length - 1)])));
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
            float[] pcoords = p.getCoordinates();
            g.fillRect(
                    ox + (pcoords[0] * Chunk.TILE_SIZE * scale) - (scale / 2),
                    oy + (pcoords[1] * Chunk.TILE_SIZE * scale) - (scale / 2),
                    scale,
                    scale
            );
        }

        g.setColor(Color.white);

    }

    public boolean isDepleted() { return particlesRemaining <= 0; }

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
        return particles.size() + ", spawn = " + pos[0] +", "+ pos[1] + ", p(0) = " + particles.get(0).getCoordinates()[0] + ", " + particles.get(0).getCoordinates()[1];
    }

}

class Particle {

    private long emissionTime, lifetime;
    private float velocity, direction;
    private float[] startPosition, startOffset;
    private Color color;

    public Particle(float velocity, float direction, int lifetime, float[] startPosition, float[] startOffset, Color color) {
        this.emissionTime = System.currentTimeMillis();
        this.startPosition = startPosition;
        this.startOffset = startOffset;
        this.velocity = velocity;
        this.direction = direction;
        this.lifetime = lifetime;
        this.color = color;
    }

    public Color getColor() { return color; }
    public float percentComplete() { return (float)(System.currentTimeMillis() - emissionTime) / (float)lifetime; }
    public boolean isExpired() { return System.currentTimeMillis() - emissionTime > lifetime; }
    public float getElapsedSeconds() { return (float)(System.currentTimeMillis() - emissionTime) / 1000f; }
    public float[] getCoordinates() {
        return getRelativeCoordinates(startPosition);
    }
    private float[] getRelativeCoordinates(float[] origin) {
        float[] offset = MiscMath.getRotatedOffset(0, -(velocity * getElapsedSeconds()), direction);
        return new float[]{ origin[0] + startOffset[0] + offset[0], origin[1] + startOffset[1] + offset[1] };
    }

}

enum EmissionMode {
    RADIATE, GRAVITATE, SCATTER
}
