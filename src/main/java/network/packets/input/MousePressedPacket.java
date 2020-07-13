package network.packets.input;

import network.Packet;

public class MousePressedPacket extends Packet {

    public int button;

    public MousePressedPacket() {}

    public MousePressedPacket(int button) {
        this.button = button;
    }

}
