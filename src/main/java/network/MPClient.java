package network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import misc.MiscMath;
import network.handlers.client.*;
import network.packets.*;
import world.Camera;
import world.World;
import world.entities.components.PlayerComponent;
import world.entities.systems.MovementSystem;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MPClient {

    private static HashMap<Class, PacketHandler> packetHandlers;
    private static Client client;
    private static World world;

    private static long time, packetsReceived, packetsSent;

    private static Timer pingTimer;

    public static void init() {
        init(0, 0);
    }

    public static void init(int minLag, int maxLag) {
        client = new Client();
        time = 0;
        world = new World();
        Packet.registerPackets(client.getKryo());
        registerPacketHandlers();
        pingTimer = new Timer();
        client.addListener(new Listener.LagListener(minLag, maxLag, new Listener() {
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
                packetsReceived++;
                if (!(packet instanceof FrameworkMessage)) System.out.println("Client received: "+packet.getClass().getSimpleName());
                PacketHandler handler = packetHandlers.get(packet.getClass());
                if (handler != null) handler.handle((Packet)packet, connection);
            }
        }));
    }

    public static boolean isOpen() {
        return world != null;
    }

    public static void join(String host) {
        try {
            client.start();
            client.connect(10000, host, 6667, 6667);
            pingTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    client.updateReturnTripTime();
                }
            }, 100, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close()
    {
        pingTimer.cancel();
        packetsSent = 0;
        packetsReceived = 0;
        time = 0;
        client.close();
        world = null;
    }

    public static void update() {
        Camera.update();
        time += MiscMath.getConstant(1000, 1);
        client.getReturnTripTime();
        //update only the player
        Set<Integer> localPlayer = world.getEntities().getEntitiesWith(PlayerComponent.class).stream()
                .filter(e -> e == Camera.getTargetEntity()).collect(Collectors.toSet());
        MovementSystem.update(world, localPlayer);
    }

    public static World getWorld() {
        return world;
    }

    private static void registerPacketHandlers() {
        packetHandlers = new HashMap<>();
        packetHandlers.put(ChunkPacket.class, new ClientChunkPacketHandler());
        packetHandlers.put(EntityUpdatePacket.class, new ClientEntityUpdatePacketHandler());
        packetHandlers.put(PlayerAssignmentPacket.class, new ClientPlayerAssignmentPacketHandler());
        packetHandlers.put(RegionPacket.class, new ClientRegionPacketHandler());
        packetHandlers.put(ComponentStateChangePacket.class, new ClientComponentStateChangePacketHandler());
        packetHandlers.put(LocationUpdatePacket.class, new ClientLocationUpdatePacketHandler());
        packetHandlers.put(EntityVelocityChangedPacket.class, new ClientEntityVelocityChangedPacketHandler());
        packetHandlers.put(TimeSyncPacket.class, new ClientTimeSyncPacketHandler());
    }

    public static void sendPacket(Packet p) {
        packetsSent++;
        client.sendTCP(p);
    }

    public static long getTime() {
        return time;
    }
    public static void setTime(long t) { time = t; }
    public static int getReturnTripTime() {
        return client.getReturnTripTime();
    }

    public static long getPacketsSent() {
        return packetsSent;
    }

    public static long getPacketsReceived() {
        return packetsReceived;
    }
}
