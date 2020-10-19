package network.packets;

import network.Packet;

public class DeactivateAnimationPacket extends Packet {

    public int entityID;
    public String animationName;

    public DeactivateAnimationPacket() {}

    public DeactivateAnimationPacket(int entityID, String animationName) {
        this.entityID = entityID;
        this.animationName = animationName;
    }

}
