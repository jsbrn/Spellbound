package network.packets;

import network.Packet;

public class EntityDestroyPacket extends Packet {

    public int entityID;

    public EntityDestroyPacket(int entityID) {
        this.entityID = entityID;
    }

}
