package network.packets;

import network.Packet;
import world.entities.components.AnimatorComponent;

public class ActiveAnimationsPacket extends Packet {

    public int entityID;
    public String[] activeAnimations;

    public ActiveAnimationsPacket() {}

    public ActiveAnimationsPacket(AnimatorComponent animator) {
        this.entityID = animator.getParent();
        this.activeAnimations = animator.getActiveAnimations().toArray(new String[0]);
    }

}
