package misc;

import network.MPServer;
import org.apache.maven.monitor.event.EventDispatcher;
import world.Chunk;
import world.Region;
import world.World;

public class Location {
    
    private String regionName;
    private double[] coordinates;
    private int lookDirection;

    public Location(Location location) {
        this(location.regionName, location.coordinates[0], location.coordinates[1]);
        this.lookDirection = location.lookDirection;
    }

    public Location(String regionName, double wx, double wy) {
        this(regionName, (int)(wx / Chunk.CHUNK_SIZE), (int)(wy / Chunk.CHUNK_SIZE), wx % Chunk.CHUNK_SIZE, wy % Chunk.CHUNK_SIZE);
    }

    public Location(String regionName, double wx, double wy, int lookDirection) {
        this(regionName, wx, wy);
        this.lookDirection = lookDirection;
    }

    public Location(String regionName, int cx, int cy, double tx, double ty) {
        this.regionName = regionName;
        this.coordinates = new double[]{(cx * Chunk.CHUNK_SIZE) + tx, (cy * Chunk.CHUNK_SIZE) + ty};
        this.lookDirection = 180;
    }

    public String getRegionName() { return regionName; }

    public double[] getCoordinates() {
        return coordinates;
    }
    public int[] getChunkCoordinates() { return new int[]{(int)Math.floor(coordinates[0] / Chunk.CHUNK_SIZE), (int)Math.floor(coordinates[1] / Chunk.CHUNK_SIZE)}; }

    /**
     * Get the local coordinates (with origin being the top-left of the current chunk).
     * @return Your position in the current chunk.
     */
    public double[] getLocalCoordinates() { return new double[]{coordinates[0] % Chunk.CHUNK_SIZE, coordinates[1] % Chunk.CHUNK_SIZE}; }

    public double getIndex(Region region) { return MiscMath.getIndex(coordinates[0], coordinates[1], Integer.MAX_VALUE); }

    public double distanceTo(Location location) {
        return distanceTo(location.coordinates[0], location.coordinates[1]);
    }

    public double distanceTo(double wx, double wy) {
        return MiscMath.distance(coordinates[0], coordinates[1], wx, wy);
    }

    public boolean intersects(double wx, double wy, double width, double height) {
        return MiscMath.pointIntersectsRect(coordinates[0], coordinates[1], wx, wy, width, height);
    }

    public boolean isNear(Location l, int chunkRange) {
        int xDist = Math.abs(getChunkCoordinates()[0] - l.getChunkCoordinates()[0]);
        int yDist = Math.abs(getChunkCoordinates()[1] - l.getChunkCoordinates()[1]);
        return xDist <= chunkRange && yDist <= chunkRange;
    }

    public int angleBetween(Location location) {
        return (int)MiscMath.angleBetween(coordinates[0], coordinates[1], location.coordinates[0], location.coordinates[1]);
    }

    public void setCoordinates(double wtx, double wty) {
        this.coordinates[0] = wtx;
        this.coordinates[1] = wty;
    }

    public void addCoordinates(double wtx, double wty) {
        setCoordinates(coordinates[0] + wtx, coordinates[1] + wty);
    }

    public int getLookDirection() { return lookDirection; }
    public void lookAt(Location l) { lookAt(l.coordinates[0], l.coordinates[1]); }
    public void lookAt(double tx, double ty) { setLookDirection((int)MiscMath.angleBetween(coordinates[0], coordinates[1], tx, ty)); }
    public void setLookDirection(int degrees) { this.lookDirection = degrees % 360; }

    public String toString() {
        return regionName+"@["+coordinates[0]+", "+coordinates[1]+"] " +
                "("+getLocalCoordinates()[0]+", "+getLocalCoordinates()[1]+")";
    }

}
