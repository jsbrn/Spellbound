package network.handlers.server;

import com.esotericsoftware.kryonet.Connection;
import com.github.mathiewz.slick.Input;
import network.MPServer;
import network.Packet;
import network.PacketHandler;
import network.packets.input.KeyPressedPacket;
import network.packets.input.KeyReleasedPacket;
import world.entities.components.InputComponent;

public class ServerKeyReleasedHandler implements PacketHandler {

    @Override
    public boolean handle(Packet p, Connection from) {
        KeyReleasedPacket kpp = (KeyReleasedPacket) p;
        int entityID = MPServer.getEntityID(from);
        InputComponent input = (InputComponent)MPServer.getWorld().getEntities().getComponent(InputComponent.class, entityID);
        input.setKey(kpp.key, false);
        return true;
    }

}
