package world.entities.components;

import misc.MiscMath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import world.World;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.*;

public class VelocityComponent extends Component {

    private LinkedList<Force> forces;

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        JSONArray listOfForces = new JSONArray();
        for (Force f: forces) {
            JSONObject jsonForce = new JSONObject();
            jsonForce.put("magnitude", f.getOriginalMagnitude());
            jsonForce.put("deceleration", f.getDeceleration());
            jsonForce.put("direction", f.getDirection());
            jsonForce.put("start_time", f.getStartTime());
            listOfForces.add(jsonForce);
        }
        serialized.put("forces", listOfForces);
        return serialized;
    }

    @Override
    public Component deserialize(JSONObject object) {
        forces = new LinkedList<>();
        JSONArray listOfForces = (JSONArray)object.get("forces");
        for (Object o: listOfForces) {
            JSONObject f = (JSONObject)o;
            forces.add(new Force(
                    (double)f.get("direction"),
                    (double)f.get("magnitude"),
                    (double)f.getOrDefault("deceleration", 0.0),
                    (long)f.get("start_time")));
        }
        return this;
    }

    public void addForce(double direction, double magnitude, double deceleration, long startTime) {
        forces.add(new Force(direction, magnitude, deceleration, startTime));
    }

    /**
     * Get the direction on both axes in terms of tiles per second.
     * @return
     */
    public double[] calculateVector(long currentTime) {
        double[] dir = new double[2];
        for (int i = forces.size() - 1; i > -1; i--) {
            Force f = forces.get(i);
            dir[0] += f.getMagnitude(currentTime) * f.getDirection()[0];
            dir[1] += f.getMagnitude(currentTime) * f.getDirection()[1];
            if (f.getMagnitude(currentTime) == 0) forces.remove(i);
        }
        return dir;
    }

    @Override
    public String getID() {
        return "velocity";
    }

}

class Force {

    private double magnitude, deceleration;
    private double[] direction;
    private long startTime;

    public Force(double direction, double magnitude, double deceleration, long startTime) {
        this.magnitude = magnitude;
        this.direction = MiscMath.getRotatedOffset(0, -1, direction);
        this.deceleration = deceleration;
        this.startTime = startTime;
    }

    public double getOriginalMagnitude() {
        return magnitude;
    }

    public double getMagnitude(long currentTime) {
        double elapsedSeconds = (currentTime - startTime) / 1000f;
        return MiscMath.clamp(magnitude - (deceleration * elapsedSeconds),0, Double.MAX_VALUE);
    }

    public long getStartTime() {
        return startTime;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public double[] getDirection() {
        return direction;
    }

}
