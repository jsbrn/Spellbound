package network.packets;

import network.MPServer;
import network.Packet;
import world.entities.components.LocationComponent;

public class LocationUpdatePacket extends Packet {

    public int entityID;
    public double wx, wy;

    public LocationUpdatePacket() {}

    public LocationUpdatePacket(int entityID) {
        this.entityID = entityID;
        LocationComponent loc = (LocationComponent)MPServer.getWorld().getEntities().getComponent(LocationComponent.class, entityID);
        this.wx = loc.getLocation().getCoordinates()[0];
        this.wy = loc.getLocation().getCoordinates()[1];
    }

}
