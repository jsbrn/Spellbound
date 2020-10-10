package network.handlers.client;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import misc.MiscMath;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.EntityUpdatePacket;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;
import world.entities.systems.MovementSystem;

public class ClientEntityUpdatePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {

        EntityUpdatePacket esp = (EntityUpdatePacket)p;
        LocationComponent lc = (LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, esp.entityID);
        VelocityComponent vc = (VelocityComponent) MPClient.getWorld().getEntities().getComponent(VelocityComponent.class, esp.entityID);

        MPClient.getWorld().spawnEntity(esp.entityID, Assets.json(esp.entityJSON), null);

        return true;
    }

}
