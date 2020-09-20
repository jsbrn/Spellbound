package main;

import misc.MiscMath;
import network.MPServer;

public class HeadlessLauncher {

    public static void main(String args[]) {
        MiscMath.DELTA_TIME = 16;
        MPServer.init();
        MPServer.launch(4534545);
        update();
    }

    private static void update() {
        while (true) {
            long before = System.currentTimeMillis();
            MPServer.update();
            long after = System.currentTimeMillis();
            MiscMath.DELTA_TIME = Math.max((int)(after - before), 1);
        }
    }

}
