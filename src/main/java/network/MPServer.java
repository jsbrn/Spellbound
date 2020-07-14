package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import misc.Location;
import misc.MiscMath;
import network.handlers.server.ServerJoinPacketHandler;
import network.handlers.server.ServerKeyPressedHandler;
import network.handlers.server.ServerKeyReleasedHandler;
import network.packets.ChunkPacket;
import network.packets.ComponentStateChangePacket;
import network.packets.EntityPutPacket;
import network.packets.JoinPacket;
import network.packets.input.KeyPressedPacket;
import network.packets.input.KeyReleasedPacket;
import world.Region;
import world.entities.components.Component;
import world.entities.components.LocationComponent;
import world.entities.systems.InputProcessingSystem;
import world.entities.systems.MovementSystem;
import world.events.Event;
import world.events.EventHandler;
import world.events.EventListener;
import world.events.EventManager;
import org.json.simple.JSONObject;
import world.World;
import world.events.event.ChunkGeneratedEvent;
import world.events.event.ComponentStateChangedEvent;
import world.events.event.EntityMovedEvent;
import world.events.event.EntityNearPlayerEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MPServer {

    private static Server server;
    private static HashMap<Class, PacketHandler> packetHandlers;
    private static HashMap<Connection, Integer> connectedPlayers;

    private static World world;
    private static JSONObject serverSettings;
    private static EventManager eventManager;

    public static void init() {
        eventManager = new EventManager();
        serverSettings = new JSONObject();
        connectedPlayers = new HashMap<>();
        world = new World();
        server = new Server();
        registerPacketHandlers();
        Packet.registerPackets(server.getKryo());
        registerEventHandlers();
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                connectedPlayers.remove(connection);
                world.getEntities().removeEntity(getEntityID(connection));
            }

            @Override
            public void received(Connection connection, Object packet) {
                if (!(packet instanceof FrameworkMessage)) System.out.println("Server received: "+packet.getClass().getSimpleName());
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
        connectedPlayers.clear();
        world = null;
        return true;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static World getWorld() {
        return world;
    }

    public static boolean isOpen() {
        return world != null;
    }

    public static void update() {
        world.update();
        MovementSystem.update(world, connectedPlayers.values());
        MovementSystem.pollForMovementEvents(world);
        InputProcessingSystem.update(world);
    }

    private static void registerPacketHandlers() {
        packetHandlers = new HashMap<>();
        packetHandlers.put(JoinPacket.class, new ServerJoinPacketHandler());
        packetHandlers.put(KeyPressedPacket.class, new ServerKeyPressedHandler());
        packetHandlers.put(KeyReleasedPacket.class, new ServerKeyReleasedHandler());
    }

    private static void registerEventHandlers() {
        EventListener serverListener = new EventListener()
            .on(ChunkGeneratedEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    ChunkGeneratedEvent cge = (ChunkGeneratedEvent)e;
                    server.sendToAllTCP(new ChunkPacket(cge.getChunk()));
                }
            })
            .on(ComponentStateChangedEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    ComponentStateChangedEvent csce = (ComponentStateChangedEvent)e;
                    Component changed = csce.getComponent();
                    sendToAll(getConnectionsWithinRange(changed.getParent()), new ComponentStateChangePacket(changed.getParent(), changed.getClass(), changed.serialize()));
                }
            })
            .on(EntityNearPlayerEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntityNearPlayerEvent enpe = (EntityNearPlayerEvent)e;
                    getConnection(enpe.getPlayerID()).sendTCP(new EntityPutPacket(enpe.getEntityID(), MPServer.getWorld().getEntities().serializeEntity(enpe.getEntityID())));
                }
            });
        eventManager.register(serverListener);
    }

    private static void sendToAll(Collection<Connection> connections, Packet packet) {
        for (Connection c: connections) c.sendTCP(packet);
    }

    public static Collection<Connection> getConnectionsWithinRange(int entityID) {
        LocationComponent entityLocation = (LocationComponent) MPServer.getWorld().getEntities().getComponent(LocationComponent.class, entityID);
        return connectedPlayers.entrySet().stream().filter(entry -> {
            int pID = entry.getValue();
            Region reg = MPServer.getWorld().getRegion(entityLocation.getLocation().getRegionName());
            //keep the player if it is contained within the region around the given entity id
            return reg.getEntitiesNear(entityID, 1).contains(pID);
        }).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public static void assignTo(Connection c, int entityID) {
        connectedPlayers.put(c, entityID);
    }

    public static int getEntityID(Connection c) {
        return connectedPlayers.get(c);
    }

    public static Connection getConnection(int playerEntityID) {
        return connectedPlayers.entrySet().stream()
                .filter(entry -> entry.getValue() == playerEntityID).map(entry -> entry.getKey())
                .findFirst().orElse(null);
    }

    /* SOME GLOBAL SERVER ACTIONS THAT NEED TO BE SEPARATED FROM THE CLIENT*/

    //TODO: better way to spawn in entities and players
    public static int spawnEntity(JSONObject entity, Location location, boolean isPlayer) {
        int entityID = world.getEntities().createEntity(entity);
        ((LocationComponent)world.getEntities().getComponent(LocationComponent.class, entityID)).setLocation(location);
        world.getRegion(location.getRegionName()).addEntity(entityID);

        if (isPlayer) world.getEntities().addComponent(Component.create("player"), entityID);

        for (Component component: world.getEntities().getComponents(entityID))
            eventManager.register(component.getEventListener());

        return entityID;
    }

}
