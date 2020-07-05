package network.handlers.client;;

import com.esotericsoftware.kryonet.Connection;
import network.Packet;
import network.PacketHandler;
import network.packets.ChunkPacket;

public class ClientChunkPacketHandler implements PacketHandler {

    @Override
    public boolean handle(Packet p, Connection from) {
        ChunkPacket ctp = (ChunkPacket)p;
        System.out.println("Received chunk in "+ctp.regionName);
        return true;
    }

}
