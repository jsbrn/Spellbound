package network.handlers.client;

import com.esotericsoftware.kryonet.Connection;
import network.MPClient;
import network.Packet;
import network.PacketHandler;
import network.packets.TimeSyncPacket;

public class ClientTimeSyncPacketHandler implements PacketHandler {
    @Override
    public boolean handle(Packet p, Connection from) {
        TimeSyncPacket tsp = (TimeSyncPacket)p;
        long adjustment = MPClient.getReturnTripTime() / 2; //one-way
        MPClient.setTime(tsp.serverTime + adjustment);
        return true;
    }
}
