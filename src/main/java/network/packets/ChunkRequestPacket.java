package network.packets;

import network.Packet;
import world.Chunk;

public class ChunkRequestPacket extends Packet {

    public int cx, cy;
    public String regionName;

    public ChunkRequestPacket() {}

    public ChunkRequestPacket(Chunk c) {
        this.cx = c.getCoordinates()[0];
        this.cy = c.getCoordinates()[1];
        this.regionName = c.getRegion().getName();
    }

}
