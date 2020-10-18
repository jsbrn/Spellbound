package network.handlers.client;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.ComponentStateChangePacket;
import org.json.simple.JSONObject;
import world.entities.components.Component;

public class ClientComponentStateChangePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        ComponentStateChangePacket cp = (ComponentStateChangePacket)p;
        try {
            Class componentClass = Class.forName(cp.className);
            Component c = MPClient.getWorld().getEntities().getComponent(componentClass, cp.entityID);
            if (c == null) return false;
            c.deserialize((JSONObject)Assets.json(cp.newJSONState));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
