package network.packets.input;

import network.MPClient;
import network.Packet;

public class KeyPressedPacket extends Packet {

    public int key;
    public long clientTime, ping;

    public KeyPressedPacket() {}

    public KeyPressedPacket(int key) {
        this.key = key;
        this.clientTime = MPClient.getTime();
        this.ping = MPClient.getReturnTripTime();
    }

}
