package main;

import com.esotericsoftware.kryonet.Server;
import misc.MiscMath;
import network.MPServer;

public class HeadlessLauncher {

    public static void main(String args[]) {
        MiscMath.DELTA_TIME = 16;

        MPServer.init();
        MPServer.launch(4534545, false);

        while (true) {
            long before = System.currentTimeMillis();
            MPServer.update(50);
            long after = System.currentTimeMillis();
            MiscMath.DELTA_TIME = Math.max((int) (after - before), 1);
            System.out.println(MiscMath.DELTA_TIME);
        }

    }

}
