package assets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class Assets {

    public static int GAME_SCREEN = 0;
    public static Image TILE_SPRITESHEET, PARTICLE;

    private static HashMap<Float, TrueTypeFont> fonts;
    private static HashMap<String, Image> images;

    public static void load() {
        try {
            TILE_SPRITESHEET = new Image("assets/tiles.png", false, Image.FILTER_NEAREST);
            PARTICLE = new Image("assets/particle.png", false, Image.FILTER_NEAREST);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public static Image getImage(String image) {
        if (images == null) images = new HashMap<>();
        if (images.containsKey(image)) return images.get(image);
        try {
            Image instance = new Image(image, false, Image.FILTER_NEAREST);
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

    public static String read(String internal) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(Assets.class.getResourceAsStream("/assets/"+internal)));
        String contents = "";
        try {
            while (true) {
                String line = bf.readLine();
                if (line == null) break;
                contents += line.trim()+"\n";
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

}
