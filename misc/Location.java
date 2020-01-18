package misc;

import world.Chunk;
import world.Region;

public class Location {

    private Region region;
    private double[] coordinates;
    private int lookDirection;

    public Location(Region region, double wx, double wy) {
        this(
            region,
                (int)(wx / Chunk.CHUNK_SIZE), (int)(wy / Chunk.CHUNK_SIZE),
            wx % Chunk.CHUNK_SIZE, wy % Chunk.CHUNK_SIZE);
    }

    public Location(Region region, int cx, int cy, double tx, double ty) {
        this.region = region;
        this.coordinates = new double[]{(cx * Chunk.CHUNK_SIZE) + tx, (cy * Chunk.CHUNK_SIZE) + ty};
        this.lookDirection = 0;
    }

    public Region getRegion() {
        return region;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double wtx, double wty) {
        this.coordinates[0] = wtx;
        this.coordinates[1] = wty;
    }

    public int[] getChunkCoordinates() {
        return new int[]{(int)(coordinates[0] / Chunk.CHUNK_SIZE), (int)(coordinates[1] / Chunk.CHUNK_SIZE)};
    }

    public double[] getLocalCoordinates() {
        return new double[]{coordinates[0] % Chunk.CHUNK_SIZE, coordinates[1] % Chunk.CHUNK_SIZE};
    }

    public Chunk getChunk() {
        return region.getChunk(getChunkCoordinates()[0], getChunkCoordinates()[1]);
    }

    public int getLookDirection() { return lookDirection; }
    public void lookAt(double tx, double ty) { setLookDirection((int)MiscMath.angleBetween(coordinates[0], coordinates[1], tx, ty)); }
    public void setLookDirection(int degrees) { this.lookDirection = degrees % 360; }

    public int getGlobalIndex() { return MiscMath.getIndex((int)coordinates[0], (int)coordinates[1], region.getSize() * Chunk.CHUNK_SIZE); }

    public String toString() {
        return region.getName()+"@["+coordinates[0]+", "+coordinates[1]+"] " +
                "("+getLocalCoordinates()[0]+", "+getLocalCoordinates()[1]+") " +
                "("+getGlobalIndex()+")";
    }

}
