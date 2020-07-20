package network;

import com.esotericsoftware.kryo.Kryo;
import network.packets.*;
import network.packets.input.KeyPressedPacket;
import network.packets.input.KeyReleasedPacket;
import network.packets.input.MousePressedPacket;
import network.packets.input.MouseReleasedPacket;

public class Packet {

    public static void registerPackets(Kryo kryo) {
        kryo.register(byte[].class);
        kryo.register(byte[][].class);
        kryo.register(double[].class);
        kryo.register(double[][].class);
        kryo.register(Packet.class);
        kryo.register(ChunkPacket.class);
        kryo.register(JoinPacket.class);
        kryo.register(PlayerAssignmentPacket.class);
        kryo.register(EntityUpdatePacket.class);
        kryo.register(RegionPacket.class);
        kryo.register(ComponentStateChangePacket.class);
        kryo.register(KeyPressedPacket.class);
        kryo.register(KeyReleasedPacket.class);
        kryo.register(MousePressedPacket.class);
        kryo.register(MouseReleasedPacket.class);
        kryo.register(LocationUpdatePacket.class);
        kryo.register(EntityVelocityChangedPacket.class);
        kryo.register(TimeSyncPacket.class);
    }

}
