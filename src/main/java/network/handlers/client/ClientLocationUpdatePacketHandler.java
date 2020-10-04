package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.LocationUpdatePacket;
import world.entities.components.LocationComponent;
import world.entities.systems.MovementSystem;

public class ClientLocationUpdatePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        LocationUpdatePacket lup = (LocationUpdatePacket)p;
        LocationComponent loc = (LocationComponent) MPClient.getWorld().getEntities().getComponent(LocationComponent.class, lup.entityID);
        if (loc == null) return false;
        loc.getLocation().setCoordinates(lup.wx, lup.wy);
        MovementSystem.cacheEntity(lup.entityID, MPClient.getWorld().getRegion(loc.getLocation()).getChunk(loc.getLocation()), 1);
        return true;
    }
}
