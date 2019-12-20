package assets.definitions;

import assets.Assets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class Definitions {

    private static ArrayList<TileDefinition> tileDefinitions;

    public static void load() {
        tileDefinitions = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray tiles = (JSONArray)parser.parse(Assets.read("definitions/tiles.json"));
            for (Object o: tiles) tileDefinitions.add(new TileDefinition((JSONObject)o));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static TileDefinition getTile(int id) {
        return tileDefinitions.get(id);
    }

}
