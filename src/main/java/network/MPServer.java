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
import network.packets.*;
import network.packets.input.KeyPressedPacket;
import network.packets.input.KeyReleasedPacket;
import world.Chunk;
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
import world.events.event.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MPServer {

    private static Server server;
    private static HashMap<Class, PacketHandler> packetHandlers;
    private static HashMap<Connection, Integer> connectedPlayers;

    private static World world;
    private static JSONObject serverSettings;
    private static EventManager eventManager;

    private static long time;
    private static Timer timeSyncTimer;

    public static void init() {
        init(new Server());
    }

    public static void init(Server kryoServer) {
        time = 0;
        eventManager = new EventManager();
        serverSettings = new JSONObject();
        connectedPlayers = new HashMap<>();
        world = new World();
        server = kryoServer;
        timeSyncTimer = new Timer();
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
                world.getEntities().removeEntity(getEntityID(connection));
                connectedPlayers.remove(connection);
            }

            @Override
            public void received(Connection connection, Object packet) {
                if (!(packet instanceof FrameworkMessage)) System.out.println("Server received: "+packet.getClass().getSimpleName());
                PacketHandler handler = packetHandlers.get(packet.getClass());
                if (handler != null) handler.handle((Packet)packet, connection);
            }
        });
    }

    public static boolean launch(int seed, boolean dedicatedThread) {

        try {
            server.bind(6667, 6667);
            world.generate(seed);
            timeSyncTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    connectedPlayers.keySet().forEach(conn -> conn.sendTCP(new TimeSyncPacket(time)));
                }
            }, 500, 1000);
            System.out.println("COOL");
            if (dedicatedThread) server.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean close() {
        server.close();
        connectedPlayers.clear();
        eventManager.unregisterAll();
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

    public static long getTime() { return time; }

    public static void update(int timeout) {
        try {
            update();
            server.update(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        world.update();
        time += MiscMath.getConstant(1000, 1);
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
            .on(EntityEnteredChunkEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntityEnteredChunkEvent cge = (EntityEnteredChunkEvent)e;
                    Connection connection = getConnection(cge.getEntityID());
                    //send full json update to nearby players
                    sendToAll(
                            getConnectionsWithinRange(cge.getEntityID()),
                            new EntityUpdatePacket(cge.getEntityID()));
                    if (connection != null) {
                        //if the entity is a player, send chunk data and entity updates for the surrounding region
                        Chunk c = cge.getChunk();
                        Region r = c.getRegion();
                        ArrayList<Chunk> chunks = cge.getChunk().getRegion().getChunks(c.getCoordinates()[0], c.getCoordinates()[1], 1);
                        for (Chunk a: chunks) connection.sendTCP(new ChunkPacket(a));
                        Collection<Integer> near = r.getEntitiesNear(cge.getEntityID(), 1);
                        for (Integer entity: near)
                            connection.sendTCP(new EntityUpdatePacket(entity));
                    }
                    //send a location update to all clients (ignored if they don't have the entity)
                    server.sendToAllTCP(new LocationUpdatePacket(cge.getEntityID()));
                }
            })
            .on(ComponentStateChangedEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    ComponentStateChangedEvent csce = (ComponentStateChangedEvent)e;
                    Component changed = csce.getComponent();
                    sendToAll(
                            getConnectionsWithinRange(changed.getParent()),
                            new ComponentStateChangePacket(changed.getParent(), changed.getClass(), changed.serialize()));
                }
            })
            .on(EntityVelocityChangedEvent.class, new EventHandler() {
                @Override
                public void handle(Event e) {
                    EntityVelocityChangedEvent eme = (EntityVelocityChangedEvent)e;
                    sendToAll(
                            getConnectionsWithinRange(eme.getEntity()),
                            new EntityVelocityChangedPacket(eme.getEntity()));
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

    public static void assign(Connection c, int entityID) {
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

    public static int spawnEntity(JSONObject entity, Location location, boolean isPlayer) {

        int entityID = world.spawnEntity((int)MiscMath.random(100000, 999999), entity, location);
        if (isPlayer) world.getEntities().addComponent(Component.create("player"), entityID);

        for (Component component: world.getEntities().getComponents(entityID))
            eventManager.register(component.getEventListener());

        server.sendToAllTCP(new EntityUpdatePacket(entityID));

        return entityID;
    }

}
