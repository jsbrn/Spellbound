package network.packets.input;

import network.Packet;

public class MouseReleasedPacket extends Packet {

    public int button;

    public MouseReleasedPacket() {}

    public MouseReleasedPacket(int button) {
        this.button = button;
    }

}
