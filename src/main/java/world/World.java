package world;

import assets.Assets;
import com.github.mathiewz.slick.Graphics;
import misc.Location;
import misc.MiscMath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import world.entities.Entities;
import world.entities.components.LocationComponent;
import world.entities.systems.MovementSystem;
import world.generators.region.DefaultWorldGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private static HashMap<String, Region> regions;
    private static ArrayList<Portal> portals;

    private static Integer localPlayerID;

    private static long time;
    private static int seed;
    private static double timeMultiplier;
    private static boolean paused;

    public static void init() {
        time = 0;
        timeMultiplier = 1;
        regions = new HashMap<>();
        portals = new ArrayList<>();
    }

    public static void generate(int seed) {
        World.seed = seed;
        addRegion(new Region("world", 24, new DefaultWorldGenerator(seed)));
        getRegion("world").plan();
    }

    public static void spawnPlayer(double wx, double wy, Region region) {
        localPlayerID = Entities.createEntity(Assets.json("definitions/entities/player.json", true));
        ((LocationComponent)Entities.getComponent(LocationComponent.class, localPlayerID))
                .setLocation(new Location(region, wx, wy));
        region.addEntity(localPlayerID);
    }

    public static int getSeed() {
        return seed;
    }

    public static Region addRegion(Region region) {
        regions.put(region.getName(), region);
        return region;
    }

    public static Region getRegion(String name) { return regions.get(name); }
    public static Region getRegion() {
        Location localLocation = ((LocationComponent)Entities.getComponent(LocationComponent.class, localPlayerID)).getLocation();
        return localLocation.getRegion();
    }
    public static Integer getLocalPlayer() { return localPlayerID; }

    public static void update() {
        if (paused) return;
        time += MiscMath.getConstant(1000, 1 / timeMultiplier);
        getRegion().update();
        MovementSystem.update();
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
        world.put("player", Entities.serializeEntity(localPlayerID));
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
            localPlayerID = Entities.createEntity(jsonPlayer);
            LocationComponent locC = (LocationComponent)Entities.getComponent(LocationComponent.class, localPlayerID);
            locC.setLocation(
                    new Location(
                            getRegion((String)jsonPlayer.get("region")),
                            (int)(double)jsonPlayer.get("x"),
                            (int)(double)jsonPlayer.get("y"),
                            (int)(long)jsonPlayer.get("rotation")));
            Camera.setTargetEntity(localPlayerID);
            getRegion().plan();
            for (Region r: regions.values()) { r.loadSavedChunks(); }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
