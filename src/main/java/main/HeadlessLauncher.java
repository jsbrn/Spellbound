package main;

import com.esotericsoftware.kryonet.Server;
import misc.MiscMath;
import network.MPServer;

import java.util.Scanner;

public class HeadlessLauncher {

    public static void main(String args[]) {

        Thread updateThread = new Thread(() -> {
            MiscMath.DELTA_TIME = 16;
            MPServer.init();
            MPServer.launch(4534545, false);
            while (true) {
                long before = System.currentTimeMillis();
                MPServer.update(25);
                long after = System.currentTimeMillis();
                MiscMath.DELTA_TIME = Math.max((int) (after - before), 1);
            }
        });

        //start game server update thread
        updateThread.start();

        //listen for console input
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input != null)
                MPServer.invokeCommand(input);
        }

    }

}
