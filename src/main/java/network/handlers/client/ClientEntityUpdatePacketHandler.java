package network.handlers.client;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import misc.MiscMath;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.EntityUpdatePacket;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;
import world.entities.systems.MovementSystem;

public class ClientEntityUpdatePacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {

        EntityUpdatePacket esp = (EntityUpdatePacket)p;
        MPClient.getWorld().getEntities().createEntity(esp.entityID, Assets.json(esp.entityJSON));
        LocationComponent lc = (LocationComponent)MPClient.getWorld().getEntities().getComponent(LocationComponent.class, esp.entityID);
        VelocityComponent vc = (VelocityComponent) MPClient.getWorld().getEntities().getComponent(VelocityComponent.class, esp.entityID);

        MPClient.getWorld().getRegion(lc.getLocation().getRegionName()).addEntity(esp.entityID);

        //interpolate movement
        int frames = (MPClient.getReturnTripTime() / 2) / MiscMath.DELTA_TIME;
        for (int i = 0; i < frames; i++)
            MovementSystem.moveEntity(esp.entityID, MPClient.getWorld(), false, false);

        return true;
    }

}
