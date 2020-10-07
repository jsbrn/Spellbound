package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.ChunkPacket;
import world.Chunk;
import world.Region;

public class ClientChunkPacketHandler implements PacketHandler {

    @Override
    public boolean handle(Packet p, Connection from) {
        ChunkPacket ctp = (ChunkPacket)p;
        Region region = MPClient.getWorld().getRegion(ctp.regionName);
        if (region == null) return false;
        Chunk c = region.getChunk(ctp.cx, ctp.cy);

        for (int i = 0; i < ctp.base.length; i++) {
            for (int j = 0; j < ctp.base[0].length; j++) {
                c.set(i, j, ctp.base[i][j], ctp.top[i][j]);
            }
        }

        return true;
    }

}
