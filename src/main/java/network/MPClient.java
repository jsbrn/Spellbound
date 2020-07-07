package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.handlers.client.*;
import network.packets.*;
import world.World;
import world.entities.systems.MovementSystem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MPClient {

    private static HashMap<Class, PacketHandler> packetHandlers;
    private static Client client;
    private static World world;

    private static long time;

    private static Timer pingTimer;

    public static void init() {
        client = new Client();
        time = 0;
        world = new World();
        Packet.registerPackets(client.getKryo());
        registerPacketHandlers();
        pingTimer = new Timer();
        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                connection.sendTCP(new JoinPacket());
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
            pingTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    client.updateReturnTripTime();
                }
            }, 500, 4000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close()
    {
        pingTimer.cancel();
        client.close();
    }

    public static void update() {
        client.getReturnTripTime();
        MovementSystem.update(world);
    }

    public static World getWorld() {
        return world;
    }

    private static void registerPacketHandlers() {
        packetHandlers = new HashMap<>();
        packetHandlers.put(ChunkPacket.class, new ClientChunkPacketHandler());
        packetHandlers.put(EntitySpawnPacket.class, new ClientEntitySpawnPacketHandler());
        packetHandlers.put(PlayerAssignmentPacket.class, new ClientPlayerAssignmentPacketHandler());
        packetHandlers.put(RegionPacket.class, new ClientRegionPacketHandler());
        packetHandlers.put(ComponentStateChangePacket.class, new ClientComponentStateChangePacketHandler());
    }

    public static long getTime() {
        return time;
    }

    public static int getPing() {
        return client.getReturnTripTime();
    }

}
