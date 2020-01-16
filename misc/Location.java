package misc;

import world.Chunk;
import world.Region;
import world.World;
import world.entities.Entity;

public class Location {

    private Region region;
    private Chunk chunk;
    private double[] coordinates;
    private int lookDirection;

    public Location(Region region, Chunk chunk, double tx, double ty) {
        this.region = region;
        this.chunk = chunk;
        this.coordinates = new double[]{tx, ty};
        this.lookDirection = 0;
    }

    public Region getRegion() {
        return region;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double tx, double ty) {
        this.coordinates[0] = tx;
        this.coordinates[1] = ty;
    }

    public int getLookDirection() { return lookDirection; }
    public void setLookDirection(int degrees) { this.lookDirection = degrees % 360; }

    public String toString() {
        return region.getName()+"@["+chunk.debug()+"]: "+coordinates[0]+", "+coordinates[1]+" ("+MiscMath.getTileIndex((int)coordinates[0], (int)coordinates[1])+")";
    }

}
