package assets.definitions;

import org.json.simple.JSONObject;

public class TileDefinition {

    private String name;
    private boolean collision, peeking;
    private int height;

    public TileDefinition(JSONObject from) {
        this.name = (String)from.get("name");
        this.collision = (boolean)from.get("collision");
        this.peeking = (boolean)from.get("peeking");
        this.height = (int)(long)from.get("height");
    }

    public String getName() { return name; }
    public boolean collides() { return collision; }
    public boolean peeking() { return peeking; }
    public int getHeight() { return height; }

}
