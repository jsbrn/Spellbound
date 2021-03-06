package misc;

import world.Chunk;
import world.Region;

public class Location {

    private Region region;
    private double[] coordinates;
    private int lookDirection;

    public Location(Location location) {
        this(location.region, location.coordinates[0], location.coordinates[1]);
        this.lookDirection = location.lookDirection;
    }

    public Location(Region region, double wx, double wy) {
        this(region, (int)(wx / Chunk.CHUNK_SIZE), (int)(wy / Chunk.CHUNK_SIZE), wx % Chunk.CHUNK_SIZE, wy % Chunk.CHUNK_SIZE);
    }

    public Location(Region region, double wx, double wy, int lookDirection) {
        this(region, wx, wy);
        this.lookDirection = lookDirection;
    }

    public Location(Region region, int cx, int cy, double tx, double ty) {
        this.region = region;
        this.coordinates = new double[]{(cx * Chunk.CHUNK_SIZE) + tx, (cy * Chunk.CHUNK_SIZE) + ty};
        this.lookDirection = 180;
    }

    public Region getRegion() {
        return region;
    }
    public double[] getCoordinates() {
        return coordinates;
    }
    public int[] getChunkCoordinates() { return new int[]{(int)(coordinates[0] / Chunk.CHUNK_SIZE), (int)(coordinates[1] / Chunk.CHUNK_SIZE)}; }
    public double[] getLocalCoordinates() { return new double[]{coordinates[0] % Chunk.CHUNK_SIZE, coordinates[1] % Chunk.CHUNK_SIZE}; }
    public Chunk getChunk() {
        return region.getChunk(getChunkCoordinates()[0], getChunkCoordinates()[1]);
    }
    public double getGlobalIndex() { return MiscMath.getIndex(coordinates[0], coordinates[1], region.getSize() * Chunk.CHUNK_SIZE); }

    public double distanceTo(Location location) {
        return distanceTo(location.coordinates[0], location.coordinates[1]);
    }

    public double distanceTo(double wx, double wy) {
        return MiscMath.distance(coordinates[0], coordinates[1], wx, wy);
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
        return region.getName()+"@["+coordinates[0]+", "+coordinates[1]+"] " +
                "("+getLocalCoordinates()[0]+", "+getLocalCoordinates()[1]+") " +
                "("+getGlobalIndex()+")";
    }

}
