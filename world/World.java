package world;

import org.newdawn.slick.Graphics;
import world.entities.types.humanoids.Player;
import world.generators.region.DefaultWorldGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

    private static HashMap<String, Region> regions;
    private static Player player;

    public static void init() {
        regions = new HashMap<>();
        player = new Player();
        Region world = new Region("world", 32, new DefaultWorldGenerator());
        world.addEntity(player, 1, 1, 1, 1);
        addRegion(world);
    }

    public static void addRegion(Region region) {
        regions.put(region.getName(), region);
    }

    public static Region getRegion(String name) {
        return regions.get(name);
    }

    public static Region getRegion() { return regions.get(player.getRegion()); }

    public static Player getPlayer() { return player; }

    public static void update() {
        getRegion().update();
    }

    public static void draw(float ox, float oy, float scale, Graphics g) {
        getRegion().draw(ox, oy, scale, g);
    }

}
