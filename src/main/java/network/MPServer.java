package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import network.packets.ChunkPacket;
import world.entities.systems.MovementSystem;
import world.events.EventManager;
import org.json.simple.JSONObject;
import world.World;

import java.io.IOException;
import java.util.HashMap;

public class MPServer {

    private static Server server;
    private static HashMap<Class, PacketHandler> packetHandlers;

    private static World world;
    private static JSONObject serverSettings;
    private static EventManager eventManager;

    public static void init() {
        eventManager = new EventManager();
        serverSettings = new JSONObject();
        world = new World();
        server = new Server();
        registerPacketHandlers();
        Packet.registerPackets(server.getKryo());
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                connection.sendTCP(new ChunkPacket(new byte[2][2]));
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object packet) {
                System.out.println("Server received: "+packet);
                PacketHandler handler = packetHandlers.get(packet.getClass());
                if (handler != null) handler.handle((Packet)packet, connection);
            }
        });
    }

    public static boolean launch(int seed) {
        server.start();
        try {
            server.bind(6667);
            world.generate(seed);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean close() {
        server.close();
        world = null;
        return true;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static World getWorld() {
        return world;
    }

    public static void update() {
        MovementSystem.update(world);
    }

    private static void registerPacketHandlers() {
        packetHandlers = new HashMap<>();
    }

}
