package assets;

import org.json.simple.JSONObject;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import world.Region;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class Assets {

    public static Image TILE_SPRITESHEET, PARTICLE;

    public static String ROOT_DIRECTORY = System.getProperty("user.home")+"/.spellbound";

    private static HashMap<Float, TrueTypeFont> fonts;
    private static HashMap<String, Image> images;
    private static HashMap<String, Image> cachedBuffers;

    public static void load() {
        try {
            TILE_SPRITESHEET = new Image("assets/tiles.png", false, Image.FILTER_NEAREST);
            PARTICLE = new Image("assets/particle.png", false, Image.FILTER_NEAREST);
            Assets.getCachedBuffer(16, 16);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public static Image getCachedBuffer(int w, int h) {
        String key = w+""+h;
        if (cachedBuffers == null) cachedBuffers = new HashMap<>();
        if (cachedBuffers.containsKey(key)) return cachedBuffers.get(key);
        try {
            Image instance = new Image(w, h, Image.FILTER_NEAREST);
            cachedBuffers.put(key, instance);
            return instance;
        } catch (SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Image getImage(String image) {
        return getImage(image, Image.FILTER_NEAREST);
    }

    public static Image getImage(String image, int filter) {
        if (images == null) images = new HashMap<>();
        if (images.containsKey(image)) return images.get(image);
        try {
            Image instance = new Image(image, false, filter);
            images.put(image, instance);
            return instance;
        } catch (SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TrueTypeFont getFont(float size) {
        if (fonts == null) fonts = new HashMap<>();
        if (fonts.get(size) != null) return fonts.get(size);
        Font awtFont = null;
        try {
            awtFont = Font.createFont(Font.PLAIN, Assets.class.getResourceAsStream("/assets/fonts/font.ttf"))
                    .deriveFont(size)
                    .deriveFont(Font.TRUETYPE_FONT);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TrueTypeFont font = new TrueTypeFont(awtFont, false);
        fonts.put(size, font);
        return font;
    }

    public static void write(String url, String contents) {
        try {
            File file = new File(url);
            if (!file.isDirectory()) file.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter(url);
            fileWriter.write(contents);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String url, boolean internal) {
        BufferedReader bf = null;
        String contents = "";
        try {
            bf = internal
                    ? new BufferedReader(new InputStreamReader(Assets.class.getResourceAsStream("/assets/"+ url)))
                    : new BufferedReader(new InputStreamReader(new FileInputStream(url)));
            while (true) {
                String line = bf.readLine();
                if (line == null) break;
                contents += line.trim()+"\n";
            }
            bf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

}
