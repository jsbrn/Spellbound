package assets;

import org.json.simple.JSONObject;
import org.lwjgl.opengl.Display;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings {

    private static JSONObject settings;

    private static void init() {
        settings = new JSONObject();
        settings.put("resolution", 0);
        settings.put("fps", 60);
        settings.put("vsync", false);
        settings.put("auto_save", true);
    }

    public static void load() {
        String url = Assets.ROOT_DIRECTORY+"/settings.json";
        if (!Files.exists(Paths.get(url)))
            init();
        else
            settings = Assets.json(url, false);

        save();
    }

    public static void save() {
        Assets.write(Assets.ROOT_DIRECTORY+"/settings.json", settings.toJSONString());
    }

    public static void set(String key, Object value) {
        settings.put(key, value);
    }

    public static Object get(String key) {
        return settings.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(settings.get(key)+"");
    }

}
