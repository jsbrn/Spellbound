package network.packets;

import network.Packet;
import world.Chunk;

public class ChunkPacket extends Packet {

    public String regionName;
    public byte[][] base, top;

    public ChunkPacket() {}

    public ChunkPacket(Chunk c) {
        this.base = c.getBase();
        this.top = c.getTop();
        this.regionName = c.getRegion().getName();
    }

}
