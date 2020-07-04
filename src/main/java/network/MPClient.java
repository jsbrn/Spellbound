package network;

import com.esotericsoftware.kryonet.Client;
import world.World;

public class MPClient {

    private static Client client;
    private static World world;

    public static void init() {
        client = new Client();
    }

    public static void update() {

    }

    public static World getWorld() {
        return world;
    }

}
