package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.ActiveAnimationsPacket;
import world.entities.components.AnimatorComponent;

public class ClientActiveAnimationsPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        ActiveAnimationsPacket aap = (ActiveAnimationsPacket)p;
        AnimatorComponent ac = (AnimatorComponent)MPClient.getWorld().getEntities().getComponent(AnimatorComponent.class, aap.entityID);
        if (ac == null) return false;
        System.out.println(aap.activeAnimations);
        ac.setLocalActiveAnimations(aap.activeAnimations);
        return true;
    }
}
