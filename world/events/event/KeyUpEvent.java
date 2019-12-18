package world.events.event;

import world.events.Event;

public class KeyUpEvent extends Event {

    private int key;

    public KeyUpEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}
