package network.handlers.server;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import misc.Location;
import network.MPServer;
import network.Packet;
import network.PacketHandler;
import network.packets.EntitySpawnPacket;
import network.packets.JoinPacket;
import network.packets.PlayerAssignmentPacket;
import network.packets.RegionPacket;
import world.Region;

public class ServerJoinPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        for (Region r: MPServer.getWorld().getRegions()) from.sendTCP(new RegionPacket(r.getName()));
        int newID = MPServer.spawnEntity(Assets.json("definitions/entities/player.json", true), new Location("world", 1, 0));
        from.sendTCP(new PlayerAssignmentPacket(newID));
        return true;
    }

}
