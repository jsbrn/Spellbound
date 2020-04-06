package world;

import assets.CSVReader;
import org.json.simple.JSONObject;

public class Tiles {

    private static CSVReader reader;

    public static CSVReader reader() {
        if (reader == null) reader = new CSVReader("definitions/tiles.csv", ';');
        return reader;
    }

    public static String getName(int id) { return reader().get("Name", id+1); }
    public static boolean collides(int id) { return Boolean.parseBoolean(reader().get("Collision", id+1)); }
    public static boolean peeking(int id) { return Boolean.parseBoolean(reader().get("Peeking", id+1)); }
    public static int getHeight(int id) { return Integer.parseInt(reader().get("Height", id+1)); }
    public static double getSpeedMultiplier(int id) { return Double.parseDouble(reader().get("SpeedMultiplier", id+1)); }
    public static float getTransparency(int id) { return (float)Double.parseDouble(reader().get("Transparency", id+1)); }

}
