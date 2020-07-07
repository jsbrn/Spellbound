package network;

import com.esotericsoftware.kryo.Kryo;
import network.packets.*;
import world.entities.components.Component;
import world.entities.components.HealthComponent;

public class Packet {

    public long timeSent;

    public static void registerPackets(Kryo kryo) {
        kryo.register(byte[].class);
        kryo.register(byte[][].class);
        kryo.register(Class.class);
        kryo.register(HealthComponent.class);
        kryo.register(Packet.class);
        kryo.register(ChunkPacket.class);
        kryo.register(JoinPacket.class);
        kryo.register(PlayerAssignmentPacket.class);
        kryo.register(EntitySpawnPacket.class);
        kryo.register(RegionPacket.class);
        kryo.register(ComponentStateChangePacket.class);
    }

}
