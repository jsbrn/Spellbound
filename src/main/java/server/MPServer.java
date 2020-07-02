package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import org.json.simple.JSONObject;
import world.World;

import java.io.IOException;

public class MPServer {

    private static Server server;
    private static World world;
    private static JSONObject settings;

    public static void testServerFunctionality() {
        server = new Server();
        Client c = new Client();
        try {
            server.bind(55505);
            c.start();
            c.connect(1000, "127.0.0.1", 55505);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        System.out.println("Server started!!!");
    }

}
