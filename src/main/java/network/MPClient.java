package network;

import com.esotericsoftware.kryonet.Client;
import world.World;

import java.io.IOException;

public class MPClient {

    private static Client client;
    private static World world;

    public static void init() {
        client = new Client();
    }

    public static void join(String host) {
        try {
            client.start();
            client.connect(10000, host, 6667);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        client.close();
    }

    public static void update() {

    }

    public static World getWorld() {
        return world;
    }

}
