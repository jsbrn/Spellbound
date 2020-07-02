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
        return null;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Component deserialize(JSONObject object) {
        Region region = World.getRegion((String)object.get("region"));
        location = new Location(region, (double)object.get("x"), (double)object.get("y"));
        return this;
    }

    @Override
    public String getID() {
        return "location";
    }

}
