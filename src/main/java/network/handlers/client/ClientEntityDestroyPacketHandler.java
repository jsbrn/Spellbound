package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.EntityDestroyPacket;

public class ClientEntityDestroyPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        EntityDestroyPacket edp = (EntityDestroyPacket)p;
        MPClient.getWorld().destroyEntity(edp.entityID);
        return true;
    }
}
