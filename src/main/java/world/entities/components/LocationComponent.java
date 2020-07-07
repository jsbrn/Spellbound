package world.entities.components;

import misc.Location;
import network.MPServer;
import org.json.simple.JSONObject;
import world.Chunk;
import world.Region;
import world.events.event.EntityNearPlayerEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationComponent extends Component {

    private Location location;

    private HashMap<Integer, Boolean> lastIsNearPlayer;
    private int[] lastChunkCoordinates;

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

    public boolean hasEnteredNewChunk() {
        if (location.getChunkCoordinates()[0] != lastChunkCoordinates[0] || location.getChunkCoordinates()[1] != lastChunkCoordinates[1]) {
            //int[] diff = new int[]{location.getChunkCoordinates()[0] - lastChunkCoordinates[0], location.getChunkCoordinates()[1] - lastChunkCoordinates[1]};
            lastChunkCoordinates[0] = location.getChunkCoordinates()[0];
            lastChunkCoordinates[1] = location.getChunkCoordinates()[1];
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
        this.lastChunkCoordinates = new int[]{location.getChunkCoordinates()[0], location.getChunkCoordinates()[1]};
    }

    @Override
    public String getID() {
        return "location";
    }

}
