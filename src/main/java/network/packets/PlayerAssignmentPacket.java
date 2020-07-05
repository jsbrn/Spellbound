package network.packets;

import network.Packet;

public class PlayerAssignmentPacket extends Packet {

    public int entityID;

    public PlayerAssignmentPacket() {}

    public PlayerAssignmentPacket(int entityID) {
        this.entityID = entityID;
    }

}
