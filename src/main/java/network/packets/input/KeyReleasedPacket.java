package network.packets.input;

import network.MPClient;
import network.Packet;

public class KeyReleasedPacket extends Packet {

    public int key, ping;
    public long clientTime;

    public KeyReleasedPacket() {}

    public KeyReleasedPacket(int key) {
        this.clientTime = MPClient.getTime();
        this.ping = MPClient.getReturnTripTime();
        this.key = key;
    }

}
