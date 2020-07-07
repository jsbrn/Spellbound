package world.entities.components;

import misc.Location;
import org.json.simple.JSONObject;
import world.Region;
import world.World;

public class LocationComponent extends Component {

    private int[] lastChunkCoordinates;
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

    public boolean hasEnteredNewChunk() {
        if (location.getChunkCoordinates()[0] != lastChunkCoordinates[0] || location.getChunkCoordinates()[1] != lastChunkCoordinates[1]) {
            lastChunkCoordinates[0] = location.getChunkCoordinates()[0];
            lastChunkCoordinates[1] = location.getChunkCoordinates()[1];
            return true;
        }
        return false;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Component deserialize(JSONObject object) {
        location = new Location((String)object.get("region"), (double)object.get("x"), (double)object.get("y"));
        lastChunkCoordinates = new int[]{location.getChunkCoordinates()[0], location.getChunkCoordinates()[1]};
        return this;
    }

    @Override
    public String getID() {
        return "location";
    }

}
