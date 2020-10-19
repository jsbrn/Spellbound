package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.ActivateAnimationPacket;
import network.packets.DeactivateAnimationPacket;
import world.entities.components.AnimatorComponent;

public class ClientDeactivateAnimationPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        DeactivateAnimationPacket aap = (DeactivateAnimationPacket) p;
        AnimatorComponent ac = (AnimatorComponent)MPClient.getWorld().getEntities().getComponent(AnimatorComponent.class, aap.entityID);
        if (ac == null) return false;
        ac.removeLocalContext(aap.animationName);
        return true;
    }
}
