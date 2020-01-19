package world;

public class Portal {

    private Region destination;
    private String name, destinationName;

    private Chunk chunk;
    private int[] exitDirection, coordinates;

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

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int x, int y) {
        this.coordinates = new int[]{x, y};
    }

}