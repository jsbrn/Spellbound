package world.entities.components;

import misc.MiscMath;
import misc.annotations.ClientExecution;
import misc.annotations.ServerClientExecution;
import misc.annotations.ServerExecution;
import network.MPServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.events.event.ComponentStateChangedEvent;

import java.util.LinkedList;

public class VelocityComponent extends Component {

    private LinkedList<Force> forces;
    private Force constant; //controlled movement
    private double baseSpeed;

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        JSONArray listOfForces = new JSONArray();
        JSONObject constantJSON = new JSONObject();
        constantJSON.put("direction", constant.getDirection());
        constantJSON.put("magnitude", constant.getOriginalMagnitude());
        for (Force f: forces) {
            JSONObject jsonForce = new JSONObject();
            jsonForce.put("magnitude", f.getOriginalMagnitude());
            jsonForce.put("deceleration", f.getDeceleration());
            jsonForce.put("direction", f.getDirection());
            listOfForces.add(jsonForce);
        }
        serialized.put("base_speed", baseSpeed);
        serialized.put("constant", constantJSON);
        serialized.put("forces", listOfForces);
        return serialized;
    }

    @Override
    public void deserialize(JSONObject object) {
        forces = new LinkedList<>();
        JSONObject constantJSON = (JSONObject)object.getOrDefault("constant", new JSONObject());
        constant = new Force((double)constantJSON.getOrDefault("direction", 0.0d), (double)constantJSON.getOrDefault("magnitude", 0.0d), 0);
        JSONArray listOfForces = (JSONArray)object.get("forces");
        for (Object o: listOfForces) {
            JSONObject f = (JSONObject)o;
            forces.add(new Force(
                    (double)f.get("direction"),
                    (double)f.get("magnitude"),
                    (double)f.getOrDefault("deceleration", 0.0)));
        }
        baseSpeed = (double)object.getOrDefault("base_speed", 1.0d);
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    @ServerExecution
    public void setConstant(double direction, double magnitude) {
        if (direction != constant.getDirection() || magnitude != constant.getMagnitude()) {
            constant = new Force(direction, magnitude, 0);
            MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
        }
    }

    @ServerExecution
    public void addForce(double direction, double magnitude, double deceleration) {
        forces.add(new Force(direction, magnitude, deceleration));
        MPServer.getEventManager().invoke(new ComponentStateChangedEvent(this));
    }

    /**
     * Get the direction on both axes in terms of tiles per second.
     * @return
     */
    @ServerClientExecution
    public double[] calculateVector(long currentTime) {
        double[] dir = new double[]{
                constant.getMagnitude() * constant.getDirections()[0],
                constant.getMagnitude() * constant.getDirections()[1]
        };
        for (int i = forces.size() - 1; i > -1; i--) {
            Force f = forces.get(i);
            if (f.getMagnitude() == 0) {
                forces.remove(i);
                continue;
            }
            f.updateMagnitude();
            dir[0] += f.getMagnitude() * f.getDirections()[0];
            dir[1] += f.getMagnitude() * f.getDirections()[1];
        }
        return dir;
    }

    @Override
    public String getID() {
        return "velocity";
    }

}

class Force {

    private double direction, magnitude, deceleration;
    private double[] directions;

    public Force(double direction, double magnitude, double deceleration) {
        this.magnitude = magnitude;
        this.direction = direction;
        this.directions = MiscMath.getRotatedOffset(0, -1, direction);
        this.deceleration = deceleration;
    }

    public double getOriginalMagnitude() {
        return magnitude;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void updateMagnitude() {
        magnitude -= MiscMath.getConstant(deceleration, 1);
        magnitude = MiscMath.clamp(magnitude, 0, Double.MAX_VALUE);
    }

    public double getDeceleration() {
        return deceleration;
    }

    public double getDirection() {
        return direction;
    }

    public double[] getDirections() {
        return directions;
    }

}
