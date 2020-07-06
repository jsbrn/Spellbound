package world;

import com.github.mathiewz.slick.Graphics;
import misc.Location;
import misc.MiscMath;
import network.MPClient;
import org.json.simple.JSONObject;
import world.entities.Entities;
import world.generation.region.OverworldGenerator;

import java.util.ArrayList;
import java.util.Collection;
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
        addRegion(new Region("world", new OverworldGenerator(seed)));
    }

    public Entities getEntities() {
        return entities;
    }

    public int getSeed() {
        return seed;
    }

    public Region addRegion(Region region) {
        regions.put(region.getName(), region);
        region.setWorld(this);
        return region;
    }

    public Collection<Region> getRegions() {
        return regions.values();
    }

    public Region getRegion(String name) { return regions.get(name); }

    public Region getRegion(Location location) { return getRegion(location.getRegionName()); }

    public void update() {
        time += MiscMath.getConstant(1000, 1);
        for (Region r: regions.values()) r.update(); //updating the region just handles chunk generation as players move around
    }

    public long getCurrentTime() { return time; }

    public void draw(float scale, Graphics g) {
        MPClient.getWorld().getRegion(Camera.getLocation()).draw(scale, g);
    }

    public void drawDebug(float scale, Graphics g) { MPClient.getWorld().getRegion(Camera.getLocation()).drawDebug(scale, g); }

}
