package network;

import com.esotericsoftware.kryonet.Connection;
import network.Packet;

public interface PacketHandler {

    /**
     * Handle the packet received (from the perspective of either the server or the client).
     * @param p The Packet :)
     * @param from The Connection that the packet was received from.
     * @return A boolean indicating whether or not an action was taken.
     */
    boolean handle(Packet p, Connection from);

}
