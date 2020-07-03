package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import events.EventDispatcher;
import org.json.simple.JSONObject;
import world.World;

import java.io.IOException;

public class Host {

    private Server server;
    private World world;
    private JSONObject serverSettings;
    private EventDispatcher eventDispatcher;

    public Host() {
        this.eventDispatcher = new EventDispatcher();
        this.server = new Server();
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
