package network.packets;

import network.Packet;

public class TimeSyncPacket extends Packet {

    public long serverTime;

    public TimeSyncPacket() {}

    public TimeSyncPacket(long serverTime) {
        this.serverTime = serverTime;
    }

}
