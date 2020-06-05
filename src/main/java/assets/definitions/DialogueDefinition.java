package assets.definitions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DialogueDefinition {

    private String id;
    private String[] texts;
    private JSONObject[] options;

    public DialogueDefinition(JSONObject from) {
        id = (String)from.get("id");
        JSONArray t = (JSONArray)from.get("texts");
        JSONArray o = (JSONArray)from.get("options");
        texts = new String[t.size()];
        options = new JSONObject[o.size()];
        for (int i = 0; i < t.size(); i++) texts[i] = (String)t.get(i);
        for (int i = 0; i < o.size(); i++) options[i] = (JSONObject) o.get(i);
    }

    public String getID() { return id; }

    public String getRandomText() {
        int r = (int)(Math.random() * texts.length);
        return texts[r];
    }

    public int getOptionCount() { return options.length; }

    public String getOptionText(int i) {
        return (String)options[i].get("label");
    }

    public DialogueDefinition getOptionDestination(int i) {
        if (i < 0 || i >= options.length) return null;
        return Definitions.getDialogue((String)options[i].get("pointsTo"));
    }

}
