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

public class Assets {

    public static int GAME_SCREEN = 0;
    public static Image TILE_SPRITESHEET, PARTICLE;
    public static TrueTypeFont FONT;

    public static void load() {
        try {
            TILE_SPRITESHEET = new Image("assets/tiles.png", false, Image.FILTER_NEAREST);
            PARTICLE = new Image("assets/particle.png", false, Image.FILTER_NEAREST);
            Font awtFont = Font.createFont(Font.PLAIN, Assets.class.getResourceAsStream("/assets/fonts/font.ttf"))
                    .deriveFont(14f)
                    .deriveFont(Font.TRUETYPE_FONT);
            //new Font("Arial", Font.PLAIN, 8 + ((pass - 6) * 4));
            FONT = new TrueTypeFont(awtFont, false);
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String internal) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(Assets.class.getResourceAsStream("/assets/"+internal)));
        String contents = "";
        try {
            while (true) {
                String line = bf.readLine();
                if (line == null) break;
                contents += line;
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }

}
