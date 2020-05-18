package world;

import assets.Assets;
import misc.Location;
import misc.MiscMath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.Graphics;
import world.entities.types.humanoids.Player;
import world.generators.region.DefaultWorldGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class World {

    private static HashMap<String, Region> regions;
    private static ArrayList<Portal> portals;
    private static Player player;

    private static long time;
    private static int seed;
    private static double timeMultiplier;
    private static boolean paused;

    public static void init(Player p) {
        time = 0;
        timeMultiplier = 1;
        regions = new HashMap<>();
        portals = new ArrayList<>();
        player = p == null ? new Player() : p;
    }

    public static void generate(int seed) {
        World.seed = seed;
        addRegion(new Region("world", 24, new DefaultWorldGenerator(seed)));
        getRegion("world").plan();
    }

    public static void spawnPlayer(int x, int y, int lookDirection, String region_name) {
        player.moveTo(new Location(getRegion(region_name), x + 0.5, y + 0.5, lookDirection));
        player.getLocation().setLookDirection(180);
        Camera.setTarget(player);
    }

    public static int getSeed() {
        return seed;
    }

    public static Region addRegion(Region region) {
        regions.put(region.getName(), region);
        return region;
    }

    public static Region getRegion(String name) { return regions.get(name); }
    public static Region getRegion() { return player.getLocation().getRegion(); }
    public static Player getLocalPlayer() { return player; }

    public static void update() {
        if (paused) return;
        time += MiscMath.getConstant(1000, 1 / timeMultiplier);
        getRegion().update();
    }

    public static boolean exists() {
        return player != null;
    }
    public static boolean isPaused() { return paused; }
    public static void setPaused(boolean p) {
        paused = p;
    }
    public static void setTimeMultiplier(double tm) { timeMultiplier = tm; }
    public static double getTimeMultiplier() { return timeMultiplier; }
    public static long getCurrentTime() { return time; }

    public static void draw(float scale, Graphics g) {
        getRegion().draw(scale, g);
    }

    public static void drawDebug(float scale, Graphics g) { getRegion().drawDebug(scale, g); }

    public static void save() {
        JSONObject world = serialize();
        for (Region r: regions.values()) r.saveAllChunks();
        Assets.write(Assets.ROOT_DIRECTORY+"/world/world.json", world.toJSONString());
    }

    private static JSONObject serialize() {
        JSONObject world = new JSONObject();
        world.put("seed", seed);
        world.put("player", getLocalPlayer().serialize());
        return world;
    }

    public static void load() {
        File f = new File(Assets.ROOT_DIRECTORY+"/world/world.json");
        JSONParser parser = new JSONParser();
        try {
            JSONObject world = (JSONObject)parser.parse(new FileReader(f));
            seed = new Long((long)world.get("seed")).intValue();
            generate(seed);
            JSONObject jsonPlayer = (JSONObject)world.get("player");
            spawnPlayer((int)(double)jsonPlayer.get("x"), (int)(double)jsonPlayer.get("y"), (int)(long)jsonPlayer.get("rotation"), (String)jsonPlayer.get("region"));
            getLocalPlayer().deserialize(jsonPlayer);
            for (Region r: regions.values()) r.loadSavedChunks();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
