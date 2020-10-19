package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.ActivateAnimationPacket;
import world.entities.components.AnimatorComponent;

public class ClientActivateAnimationPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        ActivateAnimationPacket aap = (ActivateAnimationPacket)p;
        AnimatorComponent ac = (AnimatorComponent)MPClient.getWorld().getEntities().getComponent(AnimatorComponent.class, aap.entityID);
        if (ac == null) return false;
        ac.addLocalContext(aap.animationName);
        return true;
    }
}
