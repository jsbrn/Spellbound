package world.entities.components;

import misc.Location;
import misc.annotations.ServerExecution;
import network.MPServer;
import org.json.simple.JSONObject;
import world.Chunk;
import world.Region;
import world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationComponent extends Component {

    private Location location;

    private HashMap<Integer, Boolean> lastIsNearPlayer;
    private int[] lastTileCoordinates, lastChunkCoordinates;

    public LocationComponent() {
        this.lastIsNearPlayer = new HashMap<>();
        this.lastChunkCoordinates = new int[2];
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

    public void moveTo(World world, Location location) {
        Location old = new Location(this.location);
        this.location = location;
        Region oldRegion = world.getRegion(old);
        oldRegion.removeEntity(getParent());
    }

    public boolean hasEnteredNewTile() {
        if ((int)Math.floor(location.getCoordinates()[0]) != lastTileCoordinates[0] || (int)Math.floor(location.getChunkCoordinates()[1]) != lastTileCoordinates[1]) {
            //int[] diff = new int[]{location.getChunkCoordinates()[0] - lastChunkCoordinates[0], location.getChunkCoordinates()[1] - lastChunkCoordinates[1]};
            lastTileCoordinates[0] = (int)Math.floor(location.getCoordinates()[0]);
            lastTileCoordinates[1] = (int)Math.floor(location.getChunkCoordinates()[1]);
            return true;
        }
        return false;
    }

    @ServerExecution
    public Chunk hasEnteredNewChunk() {
        if (location.getChunkCoordinates()[0] != lastChunkCoordinates[0] || location.getChunkCoordinates()[1] != lastChunkCoordinates[1]) {
            //int[] diff = new int[]{location.getChunkCoordinates()[0] - lastChunkCoordinates[0], location.getChunkCoordinates()[1] - lastChunkCoordinates[1]};
            lastChunkCoordinates[0] = location.getChunkCoordinates()[0];
            lastChunkCoordinates[1] = location.getChunkCoordinates()[1];
            return MPServer.getWorld().getRegion(location).getChunk(location);
        }
        return null;
    }

    @ServerExecution
    public boolean hasApproachedPlayer(int playerEntity, int qualifyingRadius) {
        if (playerEntity == getParent()) return false;
        Location ploc = ((LocationComponent) MPServer.getWorld().getEntities().getComponent(LocationComponent.class, playerEntity)).getLocation();
        boolean current = location.distanceTo(ploc) < Chunk.CHUNK_SIZE * qualifyingRadius * Chunk.TILE_SIZE;
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
