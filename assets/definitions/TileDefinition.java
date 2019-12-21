package assets.definitions;

import org.json.simple.JSONObject;

public class TileDefinition {

    private String name;
    private boolean collision, peeking, solid;
    private int height;
    private double speedMultiplier;

    public TileDefinition(JSONObject from) {
        this.name = (String)from.get("name");
        this.collision = (boolean)from.get("collision");
        this.peeking = (boolean)from.get("peeking");
        this.solid = (boolean)from.get("solid");
        this.height = ((Number)from.get("height")).intValue();
        this.speedMultiplier = ((Number)from.get("speedMultiplier")).doubleValue();
    }

    public String getName() { return name; }
    public boolean collides() { return collision; }
    public boolean peeking() { return peeking; }
    public boolean solid() { return solid; }
    public int getHeight() { return height; }
    public double getSpeedMultiplier() { return speedMultiplier; }

}
