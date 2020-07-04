package network;

import com.esotericsoftware.kryonet.Server;
import world.entities.systems.MovementSystem;
import world.events.EventManager;
import org.json.simple.JSONObject;
import world.World;

public class MPServer {

    private static Server server;
    private static World world;
    private static JSONObject serverSettings;
    private static EventManager eventManager;

    public void init() {
        eventManager = new EventManager();
        world = new World();
        server = new Server();
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

    public static void testServerFunctionality() {
//        server = new Server();
//        server.start();
//        Client c = new Client();
//        try {
//            server.bind(55505);
//            c.start();
//            c.connect(1000, "127.0.0.1", 55505);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Server started!!!");
    }

}
