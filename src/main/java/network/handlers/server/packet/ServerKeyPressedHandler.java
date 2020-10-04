package network.handlers.server.packet;

import com.esotericsoftware.kryonet.Connection;
import com.github.mathiewz.slick.Input;
import misc.MiscMath;
import network.MPClient;
import network.MPServer;
import network.Packet;
import network.PacketHandler;
import network.packets.input.KeyPressedPacket;
import world.entities.components.InputComponent;
import world.entities.components.LocationComponent;
import world.entities.systems.MovementSystem;
import world.events.event.PlayerInteractEvent;

public class ServerKeyPressedHandler implements PacketHandler {

    @Override
    public boolean handle(Packet p, Connection from) {
        KeyPressedPacket kpp = (KeyPressedPacket)p;
        int entityID = MPServer.getEntityID(from);

        //server-side movement interpolation
        long timePassed = kpp.ping / 2;
        long frames = timePassed / MiscMath.DELTA_TIME;
        for (int i = 0; i < frames; i++)
            MovementSystem.backtrack(MPServer.getEntityID(from), MPClient.getWorld());

        InputComponent input = (InputComponent)MPServer.getWorld().getEntities().getComponent(InputComponent.class, entityID);
        input.setKey(kpp.key, true);
        if (kpp.key == Input.KEY_E) {
            //MPServer.getEventManager().invoke(new PlayerInteractEvent());
        }
        return true;
    }

}
