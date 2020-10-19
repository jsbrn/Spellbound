package network.packets;

import network.Packet;
import world.entities.components.AnimatorComponent;

public class ActivateAnimationPacket extends Packet {

    public int entityID;
    public String animationName;

    public ActivateAnimationPacket() {}

    public ActivateAnimationPacket(int entityID, String animationName) {
        this.entityID = entityID;
        this.animationName = animationName;
    }

}
