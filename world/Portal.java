package world;

import org.json.simple.JSONObject;

public class Portal {

    private Region destination;
    private String name, destinationName;

    private boolean directionalEntrance;
    private int[] exitDirection, coordinates;

    public Portal(String name, int dx, int dy, boolean directionalEntrance, Region destination, String destination_name) {
        this.destination = destination;
        this.destinationName = destination_name;
        this.name = name;
        this.directionalEntrance = directionalEntrance;
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

    public int[] getCoordinates() { return coordinates; }
    public void setCoordinates(int x, int y) {
        this.coordinates = new int[]{x, y};
    }

    public boolean isEntranceDirectional() { return directionalEntrance; }

}