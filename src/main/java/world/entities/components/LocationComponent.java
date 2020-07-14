package world.entities.components;

import misc.Location;
import network.MPServer;
import org.json.simple.JSONObject;
import world.Region;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationComponent extends Component {

    private Location location;

    private HashMap<Integer, Boolean> lastIsNearPlayer;
    private int[] lastTileCoordinates;

    public LocationComponent() {
        this.lastIsNearPlayer = new HashMap<>();
    }

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

    public boolean hasEnteredNewTile() {
        if (Math.floor(location.getCoordinates()[0]) != lastTileCoordinates[0] || Math.floor(location.getChunkCoordinates()[1]) != lastTileCoordinates[1]) {
            //int[] diff = new int[]{location.getChunkCoordinates()[0] - lastChunkCoordinates[0], location.getChunkCoordinates()[1] - lastChunkCoordinates[1]};
            lastTileCoordinates[0] = (int)Math.floor(location.getCoordinates()[0]);
            lastTileCoordinates[1] = (int)Math.floor(location.getChunkCoordinates()[1]);
            return true;
        }
        return false;
    }

    public boolean hasApproachedPlayer(int playerEntity) {
        if (playerEntity == getParent()) return false;
        Location ploc = ((LocationComponent) MPServer.getWorld().getEntities().getComponent(LocationComponent.class, playerEntity)).getLocation();
        Region preg = MPServer.getWorld().getRegion(ploc.getRegionName());
        ArrayList<Integer> entities = preg.getEntitiesNear(playerEntity, 1);
        boolean current = entities.contains(getParent());
        boolean last = lastIsNearPlayer.get(playerEntity) != null && lastIsNearPlayer.get(playerEntity);
        if (current != last) {
            lastIsNearPlayer.put(playerEntity, current);
            if (current) return true;
        }
        return false;
    }

    @Override
    public void deserialize(JSONObject object) {
        location = new Location((String)object.get("region"), (double)object.get("x"), (double)object.get("y"));
        this.lastTileCoordinates = new int[]{location.getChunkCoordinates()[0], location.getChunkCoordinates()[1]};
    }

    @Override
    public String getID() {
        return "location";
    }

}
