package world;

import misc.Location;
import misc.MiscMath;
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

    private static long time;
    private static double timeMultiplier;
    private static boolean paused;

    public static void init() {
        time = 0;
        timeMultiplier = 1;
        regions = new HashMap<>();
        player = new Player();
        addRegion(new Region("world", 32, new DefaultWorldGenerator()));
        Region player_home = addRegion(new Region("player_home", 1, new PlayerHomeRegionGenerator()));
        player.moveTo(new Location(player_home, 0, 0, Chunk.CHUNK_SIZE/2 + 0.5f, Chunk.CHUNK_SIZE/2 - 1 + 0.5f));
        player.getLocation().setLookDirection(180);
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

    public static Player getLocalPlayer() { return player; }

    public static void update() {
        if (paused) return;
        time += MiscMath.getConstant(1000, 1 / timeMultiplier);
        getRegion().update();
    }

    public static boolean isPaused() { return paused; }
    public static void setPaused(boolean p) {
        paused = p;
    }

    public static void setTimeMultiplier(double tm) { timeMultiplier = tm; }

    public static double getTimeMultiplier() { return timeMultiplier; }

    public static long getCurrentTime() { return time; }

    public static void draw(float scale, Graphics g, boolean debug) {
        getRegion().draw(scale, g, debug);
        if (debug) drawDebug(scale, g);
    }

    public static void drawDebug(float scale, Graphics g) { getRegion().drawDebug(scale, g); }

}
