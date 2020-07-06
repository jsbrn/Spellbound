package network;

import com.esotericsoftware.kryo.Kryo;
import network.packets.*;

public class Packet {

    public static void registerPackets(Kryo kryo) {
        kryo.register(byte[].class);
        kryo.register(byte[][].class);
        kryo.register(Packet.class);
        kryo.register(ChunkPacket.class);
        kryo.register(JoinPacket.class);
        kryo.register(PlayerAssignmentPacket.class);
        kryo.register(EntitySpawnPacket.class);
        kryo.register(RegionPacket.class);
    }

}
