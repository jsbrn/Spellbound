package network.handlers;;

import com.esotericsoftware.kryonet.Connection;
import network.Packet;
import network.PacketHandler;
import network.packets.ChunkPacket;

public class ClientChunkPacketHandler implements PacketHandler {

    @Override
    public boolean handle(Packet p, Connection from) {
        ChunkPacket ctp = (ChunkPacket)p;
        System.out.println("Chunk tiles array is "+ctp.tiles.length+" in size.");
        return true;
    }

}
