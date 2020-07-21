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
import world.events.event.EntityEnteredChunkEvent;

public class ServerJoinPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {

        //send region list to client
        for (Region r: MPServer.getWorld().getRegions()) {
            from.sendTCP(new RegionPacket(r.getName()));
        }

        //create entity
        Location spawn = new Location("world", 1, 0);
        int newID = MPServer.spawnEntity(Assets.json("definitions/entities/player.json", true), spawn, true);
        MPServer.assignTo(from, newID);

        //send entity and assignment packet
        Chunk chunk = MPServer.getWorld().getRegion(spawn).getChunk(spawn);
        MPServer.getEventManager().invoke(new EntityEnteredChunkEvent(newID, chunk));
        from.sendTCP(new PlayerAssignmentPacket(newID));
        return true;
    }

}
