package world;

import assets.Assets;
import com.github.mathiewz.slick.Graphics;
import misc.Location;
import misc.MiscMath;
import network.MPClient;
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

    private Entities entities;
    private HashMap<String, Region> regions;
    private ArrayList<Portal> portals;

    private long time;
    private int seed;

    public World() {
        time = 0;
        regions = new HashMap<>();
        portals = new ArrayList<>();
        entities = new Entities();
    }

    public void generate(int seed) {
        this.seed = seed;
        addRegion(new Region("world", 24, new DefaultWorldGenerator(seed)));
        getRegion("world").plan();
    }

    public Entities getEntities() {
        return entities;
    }

    public int getSeed() {
        return seed;
    }

    public Region addRegion(Region region) {
        regions.put(region.getName(), region);
        return region;
    }

    public Region getRegion(String name) { return regions.get(name); }

    public Region getRegion(Location location) { return getRegion(location.getRegionName()); }

    public void update() {
        time += MiscMath.getConstant(1000, 1);
    }

    public long getCurrentTime() { return time; }

    public void draw(float scale, Graphics g) {
        MPClient.getWorld().getRegion(Camera.getLocation()).draw(scale, g);
    }

    public void drawDebug(float scale, Graphics g) { MPClient.getWorld().getRegion(Camera.getLocation()).drawDebug(scale, g); }

    public void save() {
        JSONObject world = serialize();
        for (Region r: regions.values()) r.saveAllChunks();
        Assets.write(Assets.ROOT_DIRECTORY+"/world/world.json", world.toJSONString());
    }

    private JSONObject serialize() {
        JSONObject world = new JSONObject();
        world.put("seed", seed);
        //world.put("player", Entities.serializeEntity(localPlayerID));
        return world;
    }

    public void load() {
        File f = new File(Assets.ROOT_DIRECTORY+"/world/world.json");
        JSONParser parser = new JSONParser();
        try {
            JSONObject world = (JSONObject)parser.parse(new FileReader(f));
            seed = new Long((long)world.get("seed")).intValue();
            generate(seed);
            for (Region r: regions.values()) { r.loadSavedChunks(); }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
