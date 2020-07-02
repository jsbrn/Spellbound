package server;

import com.esotericsoftware.kryonet.Server;
import org.json.simple.JSONObject;
import world.World;

public class MPServer {

    private static Server server;
    private static World world;
    private static JSONObject settings;

    public static void testServerFunctionality() {
        server = new Server();
        server.start();
    }

}
