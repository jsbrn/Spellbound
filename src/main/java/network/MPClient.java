package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.handlers.client.ClientChunkPacketHandler;
import network.packets.ChunkPacket;
import world.World;
import world.entities.systems.MovementSystem;

import java.io.IOException;
import java.util.HashMap;

public class MPClient {

    private static HashMap<Class, PacketHandler> packetHandlers;
    private static Client client;
    private static World world;

    public static void init() {
        client = new Client();
        world = new World();
        Packet.registerPackets(client.getKryo());
        registerPacketHandlers();
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object packet) {
                System.out.println("Client received: "+packet);
                PacketHandler handler = packetHandlers.get(packet.getClass());
                if (handler != null) handler.handle((Packet)packet, connection);
            }
        });
    }

    public static void join(String host) {
        try {
            client.start();
            client.connect(10000, host, 6667);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        client.close();
    }

    public static void update() {
        MovementSystem.update(world);
    }

    public static World getWorld() {
        return world;
    }

    private static void registerPacketHandlers() {
        packetHandlers = new HashMap<>();
        packetHandlers.put(ChunkPacket.class, new ClientChunkPacketHandler());
    }

}
