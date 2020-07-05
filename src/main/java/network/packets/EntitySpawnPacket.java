package network.packets;

import network.Packet;
import org.json.simple.JSONObject;

public class EntitySpawnPacket extends Packet {

    String entityJSON;

    public EntitySpawnPacket() {}

    public EntitySpawnPacket(JSONObject entityData) {
        this.entityJSON = entityData.toJSONString();
    }

}
