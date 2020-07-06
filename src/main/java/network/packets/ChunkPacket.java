package network.packets;

import network.Packet;
import world.Chunk;

public class ChunkPacket extends Packet {

    public int cx, cy;
    public String regionName;
    public byte[][] base, top;

    public ChunkPacket() {}

    public ChunkPacket(Chunk c) {
        this.cx = c.getCoordinates()[0];
        this.cy = c.getCoordinates()[1];
        this.base = c.getBase();
        this.top = c.getTop();
        this.regionName = c.getRegion().getName();
    }

}
