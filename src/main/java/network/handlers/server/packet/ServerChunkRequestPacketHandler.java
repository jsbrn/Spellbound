package network.handlers.server.packet;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.MPServer;
import network.Packet;
import network.PacketHandler;
import network.packets.ChunkPacket;
import network.packets.ChunkRequestPacket;
import world.Chunk;

public class ServerChunkRequestPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        ChunkRequestPacket crp = (ChunkRequestPacket)p;
        Chunk c = MPServer.getWorld().getRegion(crp.regionName).getChunk(crp.cx, crp.cy);
        if (c != null)
            from.sendTCP(new ChunkPacket(c));
        return true;
    }
}
