package world.entities.components;

import misc.Location;
import org.json.simple.JSONObject;
import world.Region;
import world.World;

public class LocationComponent extends Component {

    private Location location;

    @Override
    protected void registerEvents() {

    }

    @Override
    public String getID() {
        return "location";
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
        LocationComponent component = new LocationComponent();
        Region region = World.getRegion((String)object.get("region"));
        component.location = new Location(region, (double)object.get("x"), (double)object.get("y"));
        return component;
    }

}
