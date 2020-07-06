package world.entities.components;

import misc.Location;
import org.json.simple.JSONObject;
import world.Region;
import world.World;

public class LocationComponent extends Component {

    private Location location;

    @Override
    protected void registerEventHandlers() {

    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = new JSONObject();
        serialized.put("x", location.getCoordinates()[0]);
        serialized.put("y", location.getCoordinates()[1]);
        serialized.put("region", location.getRegionName());
        return serialized;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Component deserialize(JSONObject object) {
        location = new Location((String)object.get("region"), (double)object.get("x"), (double)object.get("y"));
        return this;
    }

    @Override
    public String getID() {
        return "location";
    }

}
