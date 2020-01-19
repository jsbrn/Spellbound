package world;

import misc.Location;
import org.newdawn.slick.Graphics;
import world.entities.types.humanoids.Player;
import world.generators.region.DefaultWorldGenerator;
import world.generators.region.DungeonGenerator;
import world.generators.region.PlayerHomeRegionGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private static HashMap<String, Region> regions;
    private static Player player;

    public static void init() {
        regions = new HashMap<>();
        player = new Player();
        addRegion(new Region("world", 32, new DefaultWorldGenerator()));
        Region player_home = addRegion(new Region("player_home", 1, new PlayerHomeRegionGenerator()));
        player.moveTo(new Location(getRegion("world"), 16, 16, Chunk.CHUNK_SIZE/2 + 0.5f, Chunk.CHUNK_SIZE/2 + 0.5f));
        Camera.setTarget(player);

    }

    public static Region addRegion(Region region) {
        regions.put(region.getName(), region);
        return region;
    }

    public static Region getRegion(String name) {
        return regions.get(name);
    }

    public static Region getRegion() { return player.getLocation().getRegion(); }

    public static Player getPlayer() { return player; }

    public static void update() {
        getRegion().update();
    }

    public static void draw(float scale, Graphics g, boolean debug) {
        getRegion().draw(scale, g, debug);
        if (debug) drawDebug(scale, g);
    }

    public static void drawDebug(float scale, Graphics g) { getRegion().drawDebug(scale, g); }

}
