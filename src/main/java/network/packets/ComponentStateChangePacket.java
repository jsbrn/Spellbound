package network.packets;

import network.Packet;
import org.json.simple.JSONObject;

public class ComponentStateChangePacket extends Packet {

    public String newJSONState, className;
    public Integer entityID;

    public ComponentStateChangePacket() {}

    public ComponentStateChangePacket(Integer entityID, Class componentClass, JSONObject newState) {
        this.newJSONState = newState.toJSONString();
        this.entityID = entityID;
        this.className = componentClass.getCanonicalName();
    }

}
