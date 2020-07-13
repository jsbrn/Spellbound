package network.packets.input;

import network.Packet;

public class KeyReleasedPacket extends Packet {

    public int key;

    public KeyReleasedPacket() {}

    public KeyReleasedPacket(int key) {
        this.key = key;
    }

}
