package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import world.entities.systems.MovementSystem;
import world.events.EventManager;
import org.json.simple.JSONObject;
import world.World;

import java.io.IOException;

public class MPServer {

    private static Server server;
    private static World world;
    private static JSONObject serverSettings;
    private static EventManager eventManager;

    public static void init() {
        eventManager = new EventManager();
        serverSettings = new JSONObject();
        world = new World();
        server = new Server();
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

}
