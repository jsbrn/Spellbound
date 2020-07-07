package network.handlers.client;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.EntityPutPacket;
import world.entities.components.LocationComponent;

public class ClientEntityUpdatePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        EntityPutPacket esp = (EntityPutPacket)p;

        if (!MPClient.getWorld().getEntities().exists(esp.entityID)) {
            MPClient.getWorld().getEntities().createEntity(esp.entityID, Assets.json(esp.entityJSON));
        } else {
            MPClient.getWorld().getEntities().updateEntity(esp.entityID, Assets.json(esp.entityJSON));
        }

        LocationComponent lc = (LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, esp.entityID);
        MPClient.getWorld().getRegion(lc.getLocation().getRegionName()).addEntity(esp.entityID);

        return true;
    }
}
