package network.packets;

import network.MPServer;
import network.Packet;
import org.json.simple.JSONObject;

public class EntityUpdatePacket extends Packet {

    public long serverTime;
    public int entityID;
    public String entityJSON;

    public EntityUpdatePacket() {}

    public EntityUpdatePacket(int entityID) {
        this.entityID = entityID;
        this.entityJSON = MPServer.getWorld().getEntities().serializeEntity(entityID).toJSONString();
    }

}
