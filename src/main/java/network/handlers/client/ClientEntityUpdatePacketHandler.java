package network.handlers.client;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import misc.Location;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.EntityUpdatePacket;
import org.json.simple.JSONObject;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

public class ClientEntityUpdatePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        EntityUpdatePacket esp = (EntityUpdatePacket)p;

        MPClient.getWorld().getEntities().createEntity(esp.entityID, Assets.json(esp.entityJSON));

        LocationComponent lc = (LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, esp.entityID);
        MPClient.getWorld().getRegion(lc.getLocation().getRegionName()).addEntity(esp.entityID);

        return true;
    }
}
