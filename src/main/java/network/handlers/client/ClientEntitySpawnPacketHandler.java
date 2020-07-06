package network.handlers.client;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.EntitySpawnPacket;
import org.json.simple.JSONObject;
import world.entities.components.LocationComponent;

public class ClientEntitySpawnPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        EntitySpawnPacket esp = (EntitySpawnPacket)p;
        int entityID = MPClient.getWorld().getEntities().createEntity(Assets.json(esp.getEntityJSON()));
        LocationComponent lc = (LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, entityID);
        MPClient.getWorld().getRegion(lc.getLocation().getRegionName()).addEntity(entityID);
        return true;
    }
}
