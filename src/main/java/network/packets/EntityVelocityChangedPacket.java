package network.packets;

import misc.Force;
import network.MPServer;
import network.Packet;
import org.json.simple.JSONObject;
import world.entities.components.LocationComponent;
import world.entities.components.VelocityComponent;

import java.util.ArrayList;

public class EntityVelocityChangedPacket extends Packet {

    public int entityID;
    public long serverTime;
    public double[][] forces;
    public double[] constant;
    public double wx, wy;

    public EntityVelocityChangedPacket() {}

    public EntityVelocityChangedPacket(int entityID) {

        VelocityComponent velocityComponent = (VelocityComponent)MPServer.getWorld().getEntities().getComponent(VelocityComponent.class, entityID);
        LocationComponent locationComponent = (LocationComponent)MPServer.getWorld().getEntities().getComponent(LocationComponent.class, entityID);

        this.serverTime = MPServer.getTime();
        this.entityID = entityID;
        this.wx = locationComponent.getLocation().getCoordinates()[0];
        this.wy = locationComponent.getLocation().getCoordinates()[1];
        this.forces = new double[velocityComponent.getForces().size()][3];
        this.constant = new double[]{
                velocityComponent.getConstant().getDirection(),
                velocityComponent.getConstant().getMagnitude(),
                velocityComponent.getConstant().getDeceleration()
        };
        for (int i = 0; i < velocityComponent.getForces().size(); i++) {
            Force f = velocityComponent.getForces().get(i);
            forces[i][0] = f.getDirection();
            forces[i][1] = f.getMagnitude();
            forces[i][2] = f.getDeceleration();
        }
    }

}
