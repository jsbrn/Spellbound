package network.packets.input;

import network.Packet;

public class KeyPressedPacket extends Packet {

    public int key;

    public KeyPressedPacket() {}

    public KeyPressedPacket(int key) {
        this.key = key;
    }

}
