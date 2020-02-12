package assets.definitions;

import assets.Assets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import world.entities.magic.techniques.Techniques;

import java.util.ArrayList;
import java.util.HashMap;

public class Definitions {

    private static ArrayList<TileDefinition> tileDefinitions;
    private static HashMap<String, DialogueDefinition> dialogueDefinitions;

    public static void load() {
        tileDefinitions = new ArrayList<>();
        dialogueDefinitions = new HashMap<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray tiles = (JSONArray)parser.parse(Assets.read("definitions/tiles.json"));
            for (Object o: tiles) tileDefinitions.add(new TileDefinition((JSONObject)o));
            JSONArray dialogues = (JSONArray)parser.parse(Assets.read("definitions/dialogues.json"));
            for (Object o: dialogues) dialogueDefinitions.put((String)((JSONObject)o).get("id"), new DialogueDefinition((JSONObject)o));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static TileDefinition getTile(int id) {
        return tileDefinitions.get(id);
    }
    public static DialogueDefinition getDialogue(String id) {
        return dialogueDefinitions.get(id);
    }

}
