package world;

import assets.CSVReader;
import org.json.simple.JSONObject;

public class Tiles {

    public static final int
            AIR = 0, GRASS = 1, WELL = 2, TALL_GRASS = 3, FLOWERS = 4,
            WOOD_HOUSE_LEFT = 5, WOOD_HOUSE_MIDDLE = 6, WOOD_HOUSE_RIGHT = 7,
            TREE = 8, WOOD_WALL_NORTH = 9, WOOD_WALL_DOOR_NORTH = 10, WOOD_FLOOR = 11,
            WOOD_CORNER_NORTHEAST = 12, WOOD_WALL_EAST = 13, WOOD_WALL_WEST = 14, WOOD_CORNER_NORTHWEST = 15,
            WOOD_WALL_SOUTH = 16, WOOD_WALL_WINDOW_SOUTH = 17, STONE_WALL_NORTH = 18, STONE_WALL_DOOR_NORTH = 19,
            STONE_FLOOR = 20, STONE_CORNER_NORTHEAST = 21, STONE_WALL_EAST = 22, STONE_WALL_WEST = 23,
            STONE_CORNER_NORTHWEST = 24, STONE_WALL_SOUTH = 25, STONE_WALL_WINDOW_SOUTH = 26, BROKEN_STONE_WALL = 27,
            TRAP_DOOR = 28, STONE_LADDER = 29, STONE_COLUMN_EAST = 30, STONE_COLUMN_WEST = 31, CRACKED_STONE_FLOOR = 32,
            BROKEN_STONE_FLOOR = 33, MOSSY_STONE_FLOOR = 34, STONE_WALL_BOOKSHELF_NORTH = 35, BOOKSHELF_NORTH = 36,
            EMPTY_BOTTLES = 37, CHAIR = 38, SLEEPING_COT = 39;

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
