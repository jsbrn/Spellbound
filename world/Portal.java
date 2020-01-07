package world;

public class Portal {

    private Region destination;
    private String name, destinationName;

    private Chunk chunk;
    private int[] exitDirection, tileCoordinates;

    public Portal(String name, int dx, int dy, Region destination, String destination_name) {
        this.destination = destination;
        this.destinationName = destination_name;
        this.name = name;
        this.exitDirection = new int[]{dx, dy};
    }

    public Region getDestination() {
        return destination;
    }

    public String getName() { return name; }

    public String getDestinationName() { return destinationName; }

    public int[] getExitDirection() {
        return exitDirection;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public int[] getTileCoordinates() {
        return tileCoordinates;
    }

    public void setTileCoordinates(int x, int y) {
        this.tileCoordinates = new int[]{x, y};
    }

}