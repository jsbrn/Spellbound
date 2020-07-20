package network.handlers.server;

import assets.Assets;
import com.esotericsoftware.kryonet.Connection;
import misc.Location;
import network.MPServer;
import network.Packet;
import network.PacketHandler;
import network.packets.ChunkPacket;
import network.packets.EntityUpdatePacket;
import network.packets.PlayerAssignmentPacket;
import network.packets.RegionPacket;
import world.Chunk;
import world.Region;

public class ServerJoinPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {

        //download world to client
        for (Region r: MPServer.getWorld().getRegions()) {
            from.sendTCP(new RegionPacket(r.getName()));
            for (Chunk c: r.getChunks()) from.sendTCP(new ChunkPacket(c));
        }

        //create entity
        Location spawn = new Location("world", 1, 0);
        int newID = MPServer.spawnEntity(Assets.json("definitions/entities/player.json", true), spawn, true);

        //send entity and assignment packet
        from.sendTCP(new EntityUpdatePacket(newID, MPServer.getWorld().getEntities().serializeEntity(newID)));
        from.sendTCP(new PlayerAssignmentPacket(newID));
        MPServer.assignTo(from, newID);
        return true;
    }

}
