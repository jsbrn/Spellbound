package network;

import com.esotericsoftware.kryo.Kryo;
import network.packets.ChunkPacket;

public class Packet {

    public static void registerPackets(Kryo kryo) {
        kryo.register(byte[].class);
        kryo.register(byte[][].class);
        kryo.register(Packet.class);
        kryo.register(ChunkPacket.class);
    }

}
