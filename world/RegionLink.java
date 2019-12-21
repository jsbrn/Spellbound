package world;

public class RegionLink {

    private Region destination;
    private int[] chunkCoords, tileCoords, direction;

    public RegionLink(Region region, int cx, int cy, int tx, int ty, int dx, int dy) {
        this.destination = region;
        this.chunkCoords = new int[]{cx, cy};
        this.tileCoords = new int[]{tx, ty};
        this.direction = new int[]{dx, dy};
    }

    public Region getDestination() {
        return destination;
    }

    public int[] getChunkCoordinates() {
        return chunkCoords;
    }

    public int[] getTileCoordinates() {
        return tileCoords;
    }

    public int[] getDirection() {
        return direction;
    }

}
