package world.entities.ai.states;

import misc.Location;

import java.util.Random;

public class StayPutState extends State {

    Random rng;
    Location original;

    @Override
    public void onEnter() {
        this.rng = new Random();
        Location loc = getParent().getLocation();
        this.original = new Location(
                loc.getRegion(), loc.getCoordinates()[0], loc.getCoordinates()[1]);
        getParent().clearAllActions();
    }

    @Override
    public void update() {

    }

    @Override
    public void onExit() {

    }

}
