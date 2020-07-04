package world.events.event;

import world.events.Event;

public class KeyDownEvent extends Event {

    private int key;

    public KeyDownEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}
