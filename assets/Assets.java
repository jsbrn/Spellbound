package assets;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;

public class Assets {

    public static int GAME_SCREEN = 0;
    public static Image TILES;
    public static TrueTypeFont FONT;

    public static void load() {
        try {
            TILES = new Image("assets/tiles.png", false, Image.FILTER_NEAREST);
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

}
