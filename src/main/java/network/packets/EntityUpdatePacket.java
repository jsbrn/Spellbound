package network.packets;

import network.Packet;
import org.json.simple.JSONObject;

public class EntityUpdatePacket extends Packet {

    public long serverTime;
    public int entityID;
    public String entityJSON;

    public EntityUpdatePacket() {}

    public EntityUpdatePacket(int entityID, JSONObject entityData) {
        this.entityID = entityID;
        this.entityJSON = entityData.toJSONString();
    }

}
