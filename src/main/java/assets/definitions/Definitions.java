package assets.definitions;

import assets.Assets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public class Definitions {

    private static HashMap<String, DialogueDefinition> dialogueDefinitions;

    public static void load() {
        dialogueDefinitions = new HashMap<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray dialogues = (JSONArray)parser.parse(Assets.read("definitions/dialogues.json", true));
            for (Object o: dialogues) dialogueDefinitions.put((String)((JSONObject)o).get("id"), new DialogueDefinition((JSONObject)o));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static DialogueDefinition getDialogue(String id) {
        return dialogueDefinitions.get(id);
    }

}
