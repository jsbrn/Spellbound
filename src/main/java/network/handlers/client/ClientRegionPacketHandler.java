package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.RegionPacket;
import world.Region;
import world.generation.region.EmptyRegionGenerator;

public class ClientRegionPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        RegionPacket rp = (RegionPacket)p;
        MPClient.getWorld().addRegion(new Region(rp.regionName, new EmptyRegionGenerator(0)));
        return true;
    }
}
