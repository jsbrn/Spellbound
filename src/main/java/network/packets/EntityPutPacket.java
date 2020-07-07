package network.packets;

import network.Packet;
import org.json.simple.JSONObject;

public class EntityPutPacket extends Packet {

    public int entityID;
    public String entityJSON;

    public EntityPutPacket() {}

    public EntityPutPacket(int entityID, JSONObject entityData) {
        this.entityID = entityID;
        this.entityJSON = entityData.toJSONString();
    }

}
