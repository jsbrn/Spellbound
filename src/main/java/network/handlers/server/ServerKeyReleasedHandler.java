package network.handlers.server;

import com.esotericsoftware.kryonet.Connection;
import com.github.mathiewz.slick.Input;
import misc.MiscMath;
import network.MPClient;
import network.MPServer;
import network.Packet;
import network.PacketHandler;
import network.packets.input.KeyPressedPacket;
import network.packets.input.KeyReleasedPacket;
import world.entities.components.InputComponent;
import world.entities.systems.MovementSystem;

public class ServerKeyReleasedHandler implements PacketHandler {

    @Override
    public boolean handle(Packet p, Connection from) {
        KeyReleasedPacket kpp = (KeyReleasedPacket) p;
        int entityID = MPServer.getEntityID(from);

        //server-side movement interpolation
        long timePassed = kpp.ping / 2;
        long frames = timePassed / MiscMath.DELTA_TIME;
        for (int i = 0; i < frames; i++)
            MovementSystem.backtrack(entityID, MPClient.getWorld());

        InputComponent input = (InputComponent)MPServer.getWorld().getEntities().getComponent(InputComponent.class, entityID);
        input.setKey(kpp.key, false);
        return true;
    }

}
