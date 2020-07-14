package network.handlers.client;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.ComponentStateChangePacket;
import world.entities.components.Component;
import world.events.event.ComponentStateChangedEvent;

public class ClientComponentStateChangePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        ComponentStateChangePacket cp = (ComponentStateChangePacket)p;
        try {
            Class componentClass = Class.forName(cp.className);
            Component c = MPClient.getWorld().getEntities().getComponent(componentClass, cp.entityID);
            if (c == null) return false;
            c.deserialize(Assets.json(cp.newJSONState));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
}
