package network.packets;

import network.Packet;

public class ChunkPacket extends Packet {

    public byte[][] tiles;

    public ChunkPacket() {}

    public ChunkPacket(byte[][] tiles) {
        this.tiles = tiles;
    }

}
