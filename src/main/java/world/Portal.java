package world;

import org.newdawn.slick.Sound;

public class Portal {

    private Region destination;
    private String name, destinationName;

    private boolean directionalEntrance;
    private int[] exitDirection, coordinates;

    private Sound sound;

    public Portal(String name, int tx, int ty, int dx, int dy, boolean directionalEntrance, Region destination, String destination_name, Sound sound) {
        this.coordinates = new int[]{tx, ty};
        this.destination = destination;
        this.destinationName = destination_name;
        this.name = name;
        this.directionalEntrance = directionalEntrance;
        this.exitDirection = new int[]{dx, dy};
        this.sound = sound;
    }

    public Sound getSound() {
        return sound;
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