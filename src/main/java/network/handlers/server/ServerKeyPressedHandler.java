package network.handlers.server;

import com.esotericsoftware.kryonet.Connection;
import com.github.mathiewz.slick.Input;
import network.MPServer;
import network.Packet;
import network.PacketHandler;
import network.packets.input.KeyPressedPacket;
import world.entities.components.InputComponent;
import world.events.event.PlayerInteractEvent;

public class ServerKeyPressedHandler implements PacketHandler {

    @Override
    public boolean handle(Packet p, Connection from) {
        KeyPressedPacket kpp = (KeyPressedPacket)p;
        int entityID = MPServer.getEntityID(from);
        InputComponent input = (InputComponent)MPServer.getWorld().getEntities().getComponent(InputComponent.class, entityID);
        input.setKey(kpp.key, true);
        if (kpp.key == Input.KEY_E) {
            //MPServer.getEventManager().invoke(new PlayerInteractEvent());
        }
        return true;
    }

}
