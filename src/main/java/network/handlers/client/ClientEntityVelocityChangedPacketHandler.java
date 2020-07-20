package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import misc.MiscMath;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.EntityVelocityChangedPacket;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;
import world.entities.systems.MovementSystem;

public class ClientEntityVelocityChangedPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        EntityVelocityChangedPacket evcp = (EntityVelocityChangedPacket)p;
        LocationComponent lc = (LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, evcp.entityID);
        VelocityComponent vc = (VelocityComponent) MPClient.getWorld().getEntities().getComponent(VelocityComponent.class, evcp.entityID);

        long timePassed = MPClient.getReturnTripTime() / 2;
        long frames = timePassed / MiscMath.DELTA_TIME;

        lc.getLocation().setCoordinates(evcp.wx, evcp.wy);
        vc.deserialize(evcp.constant, evcp.forces);
        double[] vec = vc.calculateVector();
        for (int i = 0; i < frames; i++)
            MovementSystem.moveEntity(evcp.entityID, MPClient.getWorld());
        return true;
    }
}
