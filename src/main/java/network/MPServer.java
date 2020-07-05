package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import misc.Location;
import network.packets.ChunkPacket;
import network.packets.EntitySpawnPacket;
import world.entities.Entities;
import world.entities.components.Component;
import world.entities.components.LocationComponent;
import world.entities.systems.MovementSystem;
import world.events.Event;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.EventManager;
import org.json.simple.JSONObject;
import world.World;
import world.events.event.EntitySpawnEvent;

import javax.swing.text.html.parser.Entity;
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
                super.connected(connection);
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

    private static void registerEventHandlers() {
        EventListener serverListener = new EventListener()
            .on(EntitySpawnEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntitySpawnEvent ese = (EntitySpawnEvent)e;
                    server.sendToAllTCP(new EntitySpawnPacket(world.getEntities().serializeEntity(ese.getEntityID())));
                }
            });
        eventManager.register(serverListener);
    }

    /* SOME GLOBAL SERVER ACTIONS THAT NEED TO BE SEPARATED FROM THE CLIENT*/

    public static int spawnEntity(JSONObject entity, Location location) {
        int entityID = world.getEntities().createEntity(entity);
        ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entityID)).setLocation(location);
        world.getRegion(location.getRegionName()).addEntity(entityID);

        for (Component component: world.getEntities().getComponents(entityID))
            eventManager.register(component.getEventListener());

        eventManager.invoke(new EntitySpawnEvent(entityID));

        return entityID;
    }

}
