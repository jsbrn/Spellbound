package network.packets;

import network.Packet;

public class RegionPacket extends Packet {

    public String regionName;

    public RegionPacket() {}

    public RegionPacket(String regionName) {
        this.regionName = regionName;
    }

}
