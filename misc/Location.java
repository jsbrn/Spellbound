package misc;

import world.Chunk;
import world.Region;
import world.World;
import world.entities.Entity;

public class Location {

    private Region region;
    private Chunk chunk;
    private double[] coordinates;

    public Location(Region region, Chunk chunk, double tx, double ty) {
        this.region = region;
        this.chunk = chunk;
        this.coordinates = new double[]{tx, ty};
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

    public String toString() {
        return region.getName()+"@["+chunk.debug()+"]";
    }

}
