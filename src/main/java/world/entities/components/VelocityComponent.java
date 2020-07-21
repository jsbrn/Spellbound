package world.entities.components;

import misc.Force;
import misc.annotations.ClientExecution;
import misc.annotations.ServerClientExecution;
import misc.annotations.ServerExecution;
import network.MPServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.events.event.EntityVelocityChangedEvent;

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
        constantJSON.put("magnitude", constant.getMagnitude());
        for (Force f: forces) {
            JSONObject jsonForce = new JSONObject();
            jsonForce.put("magnitude", f.getMagnitude());
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

    public void deserialize(double[] constant, double[][] fs) {
        forces.clear();
        this.constant = new Force(constant[0], constant[1], constant[2]);
        for (double[] f: fs) {
            forces.add(new Force(f[0], f[1], f[2]));
        }
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    @ServerExecution
    public void setConstant(double direction, double magnitude) {
        if (direction != constant.getDirection() || magnitude != constant.getMagnitude()) {
            constant = new Force(direction, magnitude, 0);
            MPServer.getEventManager().invoke(new EntityVelocityChangedEvent(getParent()));
        }
    }

    @ClientExecution
    public void setClientConstant(double direction, double magnitude) {
        if (direction != constant.getDirection() || magnitude != constant.getMagnitude()) {
            constant = new Force(direction, 1, -magnitude);
        }
    }

    public Force getConstant() {
        return constant;
    }

    @ServerExecution
    public void addForce(double direction, double magnitude, double deceleration) {
        forces.add(new Force(direction, magnitude, deceleration));
        MPServer.getEventManager().invoke(new EntityVelocityChangedEvent(getParent()));
    }

    public LinkedList<Force> getForces() {
        return forces;
    }

    public void stop() { forces.clear(); }

    /**
     * Get the direction on both axes in terms of tiles per second. Applies acceleration to the force vectors before
     * aggregating.
     * @return A double[] describing the tiles per second on both axes.
     */
    @ServerClientExecution
    public double[] calculateVector(boolean constantsOnly, boolean backwards) {
        constant.updateMagnitude();
        double[] dir = new double[]{
                constant.getMagnitude() * constant.getDirections()[0],
                constant.getMagnitude() * constant.getDirections()[1]
        };
        if (!constantsOnly) {
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
        }
        if (backwards) {
            dir[0] = -dir[0];
            dir[1] = -dir[1];
        }
        return dir;
    }

    @Override
    public String getID() {
        return "velocity";
    }

}

